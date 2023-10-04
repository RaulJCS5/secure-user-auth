package com.example.secureuserauth.repository.jdbi

import com.example.secureuserauth.domain.*
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import org.slf4j.LoggerFactory
import com.example.secureuserauth.repository.UsersRepository
import java.time.Instant

class JdbiUsersRepository(
    private val handle: Handle
) : UsersRepository {
    override fun storeUser(username: String, passwordValidation: PasswordValidationInfo): String {
        try {
            // Insert the user
            val storeUser = handle.createUpdate(
                """
        insert into dbo.Users (username, password_validation) values (:username, :password_validation)
        """
            )
                .bind("username", username)
                .bind("password_validation", passwordValidation.validationInfo)
                .executeAndReturnGeneratedKeys()
                .mapTo<Int>()
                .one()
                .toString()

            return storeUser
        } catch (e: Exception) {
            return "Error storing user $username with error: ${e.message}"
        }
    }

    override fun getUserByUsername(username: String): User? {
        try {
            val user = handle.createQuery("select * from dbo.Users where username = :username")
                .bind("username", username)
            val userExists = user.mapTo<User>()
                .singleOrNull()
            return userExists
        } catch (e: Exception) {
            return null
        }
    }

    override fun getTokenByTokenValidationInfo(tokenValidationInfo: TokenValidationInfo): Pair<User, Token>? {
        try {
            val pairUserAndToken =
                handle.createQuery(
                    """
                select id, username, password_validation, token_validation, created_at, last_used_at
                from dbo.Users as users 
                inner join dbo.Tokens as tokens 
                on users.id = tokens.user_id
                where token_validation = :validation_information
            """
                )
                    .bind("validation_information", tokenValidationInfo.validationInfo)
                    .mapTo<UserAndTokenModel>()
                    .singleOrNull()
                    ?.userAndToken
            return pairUserAndToken
        } catch (e: Exception) {
            return null
        }
    }

    override fun isUserStoredByUsername(username: String): Boolean {
        try {
            val userExists = handle.createQuery(
                "select count(*) from dbo.Users where username = :username"
            )
                .bind("username", username)
                .mapTo<Int>()
                .single() == 1
            return userExists
        } catch (e: Exception) {
            // TODO: return a proper error message
            return false
        }
    }

    override fun createToken(token: Token, maxTokens: Int) {
        try {
            val deletions = handle.createUpdate(
                """
                delete from dbo.Tokens 
                where user_id = :user_id 
                    and token_validation in (
                        select token_validation from dbo.Tokens where user_id = :user_id 
                            order by last_used_at desc offset :offset
                    )
                """.trimIndent()
            )
                .bind("user_id", token.userId)
                .bind("offset", maxTokens - 1)
                .execute()

            logger.info("{} tokens deleted when creating new token", deletions)

            handle.createUpdate(
                """
                    insert into dbo.Tokens(user_id, token_validation, created_at, last_used_at) 
                    values (:user_id, :token_validation, :created_at, :last_used_at)
                """.trimIndent()
            )
                .bind("user_id", token.userId)
                .bind("token_validation", token.tokenValidationInfo.validationInfo)
                .bind("created_at", token.createdAt.epochSecond)
                .bind("last_used_at", token.lastUsedAt.epochSecond)
                .execute()
        } catch (e: Exception) {

        }
    }

    override fun updateTokenLastUsed(token: Token, now: Instant) {
        try {
            handle.createUpdate(
                """
                update dbo.Tokens set last_used_at = :last_used_at where token_validation = :token_validation
                """.trimIndent()
            )
                .bind("last_used_at", token.lastUsedAt.epochSecond)
                .bind("token_validation", token.tokenValidationInfo.validationInfo)
                .execute()
        } catch (e: Exception) {

        }
    }

    override fun getUserById(id: String): User? {
        try {
            val idAsInt = id.toInt()
            val user = handle.createQuery("select * from dbo.Users where id = :id")
                .bind("id", idAsInt)
            val userExists = user.mapTo<User>()
                .singleOrNull()
            return userExists
        } catch (e: Exception) {
            return null
        }
    }

    override fun invalidateToken(second: Token): Boolean {
        try {
            handle.createUpdate(
                """
                delete from dbo.Tokens where token_validation = :token_validation
                """.trimIndent()
            )
                .bind("token_validation", second.tokenValidationInfo.validationInfo)
                .execute()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override fun getTokenByUser(user: User): Token? {
        return try {
            handle.createQuery(
                """
            SELECT * FROM dbo.Tokens 
            WHERE user_id = :user_id 
            ORDER BY last_used_at DESC 
            LIMIT 1
            """.trimIndent()
            )
                .bind("user_id", user.id)
                .map { rs, _ ->
                    val tokenValidation = rs.getString("token_validation")
                    val tokenValidationInfo = TokenValidationInfo(tokenValidation)
                    val createdAt = rs.getLong("created_at")
                    val instantCreatedAt = Instant.ofEpochMilli(createdAt)
                    val lastUsedAt = rs.getLong("last_used_at")
                    val instantLastUsedAt = Instant.ofEpochMilli(lastUsedAt)
                    Token(
                        tokenValidationInfo,
                        user.id,
                        instantCreatedAt,
                        instantLastUsedAt
                    )
                }
                .singleOrNull()
        } catch (e: Exception) {
            null
        }
    }


    companion object {
        private val logger = LoggerFactory.getLogger(JdbiUsersRepository::class.java)
    }
}