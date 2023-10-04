package com.example.secureuserauth.config

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import com.example.secureuserauth.config.mappers.InstantMapper
import com.example.secureuserauth.config.mappers.PasswordValidationInfoMapper
import com.example.secureuserauth.config.mappers.TokenValidationInfoMapper

fun Jdbi.secureUserAuthConfigure(): Jdbi {
    installPlugin(KotlinPlugin())
    installPlugin(PostgresPlugin())
    registerColumnMapper(PasswordValidationInfoMapper())
    registerColumnMapper(TokenValidationInfoMapper())
    registerColumnMapper(InstantMapper())
    return this
}