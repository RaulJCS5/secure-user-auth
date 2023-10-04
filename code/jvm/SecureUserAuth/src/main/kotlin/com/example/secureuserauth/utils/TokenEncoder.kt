package com.example.secureuserauth.utils

import com.example.secureuserauth.domain.TokenValidationInfo

interface TokenEncoder {
    fun createValidationInformation(token: String): TokenValidationInfo
    fun validate(validationInfo: TokenValidationInfo, token: String): Boolean
}