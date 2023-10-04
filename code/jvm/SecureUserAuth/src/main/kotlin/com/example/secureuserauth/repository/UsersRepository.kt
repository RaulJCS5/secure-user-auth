package com.example.secureuserauth.repository

import com.example.secureuserauth.domain.PasswordValidationInfo
import com.example.secureuserauth.domain.Token
import com.example.secureuserauth.domain.TokenValidationInfo
import com.example.secureuserauth.domain.User
import java.time.Instant

interface UsersRepository {

    fun storeUser(
        username: String,
        passwordValidation: PasswordValidationInfo,
    ): String

    fun getUserByUsername(username: String): User?

    fun getTokenByTokenValidationInfo(tokenValidationInfo: TokenValidationInfo): Pair<User, Token>?

    fun isUserStoredByUsername(username: String): Boolean

    fun createToken(token: Token, maxTokens: Int)

    fun updateTokenLastUsed(token: Token, now: Instant)
    fun getUserById(id: String): User?
    fun invalidateToken(second: Token): Boolean
    fun getTokenByUser(user: User): Token?
}