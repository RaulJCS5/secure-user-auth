package com.example.secureuserauth.domain

import com.example.secureuserauth.domain.PasswordValidationInfo

data class User(
    val id: Int,
    val username: String,
    val passwordValidation: PasswordValidationInfo,
)