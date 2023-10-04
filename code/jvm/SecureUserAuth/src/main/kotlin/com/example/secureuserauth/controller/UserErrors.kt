package com.example.secureuserauth.controller

import com.example.secureuserauth.utils.Either
import com.example.secureuserauth.domain.User

sealed class UserCreationError {
    object UserAlreadyExists : UserCreationError()
    object InsecurePassword : UserCreationError()
}

typealias UserCreationResult = Either<UserCreationError, String>

sealed class TokenCreationError {
    object UserOrPasswordAreInvalid : TokenCreationError()
}

typealias TokenCreationResult = Either<TokenCreationError, String>

sealed class UserGetError {
    object UserNotFound : UserGetError()
}

typealias UserGetResult = Either<UserGetError, User>
