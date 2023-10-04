package com.example.secureuserauth.controller

import org.springframework.web.util.UriTemplate

object Uris {

    const val API = "/api"

    object Users {
        const val CREATE = "$API/users"
        const val TOKEN = "$API/users/token"
        const val GET_BY_ID = "$API/users/{id}"
        const val HOME = "$API/me"
        const val SIGN_OUT = "$API/signout"

        fun byId(id: String) = UriTemplate(GET_BY_ID).expand(id)
    }
}