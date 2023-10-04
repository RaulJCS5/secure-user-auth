package com.example.secureuserauth.domain

import com.example.secureuserauth.domain.PasswordValidationInfo
import com.example.secureuserauth.domain.Token
import com.example.secureuserauth.domain.TokenValidationInfo
import com.example.secureuserauth.domain.User
import java.time.Instant

data class UserAndTokenModel(
    val id: Int,
    val username: String,
    val passwordValidation: PasswordValidationInfo,
    val tokenValidation: TokenValidationInfo,
    val createdAt: Long,
    val lastUsedAt: Long,
) {
    val userAndToken: Pair<User, Token>
        get() = Pair(
            User(id, username, passwordValidation),
            Token(tokenValidation, id, Instant.ofEpochSecond(createdAt), Instant.ofEpochSecond(lastUsedAt))
        )
}
