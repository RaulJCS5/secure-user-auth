package com.example.secureuserauth.service

import com.example.secureuserauth.controller.*
import com.example.secureuserauth.domain.PasswordValidationInfo
import com.example.secureuserauth.domain.Token
import com.example.secureuserauth.domain.User
import com.example.secureuserauth.domain.UserLogic
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import com.example.secureuserauth.utils.Clock
import com.example.secureuserauth.utils.Either
import com.example.secureuserauth.repository.TransactionManager
import com.example.secureuserauth.utils.TokenEncoder
import java.time.Duration
import java.util.*

@Component
class UsersService(
    private val transactionManager: TransactionManager,
    private val userLogic: UserLogic,
    private val passwordEncoder: PasswordEncoder,
    private val tokenEncoder: TokenEncoder,
    private val clock: Clock,
) {
    companion object {
        private val TOKEN_ROLLING_TTL: Duration = Duration.ofHours(1)
        private val TOKEN_TTL: Duration = Duration.ofDays(1)
        private const val MAX_TOKENS = 1
    }

    fun createUser(username: String, password: String): UserCreationResult {
        if (!userLogic.isSafePassword(password)) {
            return Either.Left(UserCreationError.InsecurePassword)
        }

        val passwordValidationInfo = PasswordValidationInfo(passwordEncoder.encode(password))

        val transactionResult = transactionManager.run { transaction ->
            val usersRepository = transaction.usersRepository
            if (usersRepository.isUserStoredByUsername(username)) {
                Either.Left(UserCreationError.UserAlreadyExists)
            } else {
                val id = usersRepository.storeUser(username, passwordValidationInfo)
                Either.Right(id)
            }
        }

        return transactionResult
    }

    fun createToken(username: String, password: String): TokenCreationResult {
        if (username.isBlank() || password.isBlank()) {
            return Either.Left(TokenCreationError.UserOrPasswordAreInvalid)
        }

        val transactionResult = transactionManager.run { transaction ->
            val usersRepository = transaction.usersRepository
            val user: User = usersRepository.getUserByUsername(username) ?: return@run userNotFound()

            if (!passwordEncoder.matches(password, user.passwordValidation.validationInfo)) {
                return@run Either.Left(TokenCreationError.UserOrPasswordAreInvalid)
            }

            val token = userLogic.generateToken()
            val now = clock.now()
            val newToken = Token(
                tokenEncoder.createValidationInformation(token),
                user.id,
                now,
                now
            )
            usersRepository.createToken(newToken, MAX_TOKENS)

            Either.Right(token)
        }

        return transactionResult
    }

    fun getUserByToken(jwtToken: String): User? {
        val decodedClaims = verifyAndDecodeJwtToken(jwtToken)
        if (decodedClaims != null) {
            // Token is valid, and you can access claims here, such as subject, expiration, custom claims, etc.
            val subject = decodedClaims.subject
            val expiration = decodedClaims.expiration
            val now = Date(clock.now().toEpochMilli())
            if (expiration.before(now)) {
                // Token is expired
                // Handle accordingly
                return null
            }
            // ...
            if (!userLogic.canBeToken(subject)) {
                return null
            }
            val transactionResult = transactionManager.run { transaction ->
                val usersRepository = transaction.usersRepository
                val tokenValidationInfo = tokenEncoder.createValidationInformation(subject)
                val userAndToken = usersRepository.getTokenByTokenValidationInfo(tokenValidationInfo)
                if (userAndToken != null && isTokenStillValid(userAndToken.second)) {
                    usersRepository.updateTokenLastUsed(userAndToken.second, clock.now())
                    userAndToken.first
                } else {
                    null
                }
            }
            return transactionResult
        } else {
            // Token is invalid or expired
            // Handle accordingly
            return null
        }
    }

    private fun verifyAndDecodeJwtToken(token: String): Claims? {
        try {
            val claims = Jwts.parser()
                .setSigningKey("secret".toByteArray())
                .parseClaimsJws(token)
                .body
            return claims
        } catch (ex: MalformedJwtException) {
            // Handle invalid or expired tokens here
            return null
        }
    }

    private fun isTokenStillValid(token: Token): Boolean {
        val now = clock.now()
        return now.isBefore(token.createdAt.plus(TOKEN_TTL)) &&
                now.isBefore(token.lastUsedAt.plus(TOKEN_ROLLING_TTL))
    }

    private fun userNotFound(): TokenCreationResult {
        passwordEncoder.encode("changeit")
        return Either.Left(TokenCreationError.UserOrPasswordAreInvalid)
    }

    fun getUserById(id: String): UserGetResult {
        val transactionResult = transactionManager.run { transaction ->
            val usersRepository = transaction.usersRepository
            val user = usersRepository.getUserById(id)

            if (user != null) {
                Either.Right(user)
            } else {
                Either.Left(UserGetError.UserNotFound)
            }
        }

        return transactionResult
    }

    fun invalidateToken(user: User): Boolean {
        return transactionManager.run { transaction ->
            val usersRepository = transaction.usersRepository

            if (usersRepository.isUserStoredByUsername(user.username)) {
                val token = usersRepository.getTokenByUser(user)
                if (token != null) {
                    usersRepository.invalidateToken(token)
                    return@run true
                }
            }

            false
        }
    }
}
