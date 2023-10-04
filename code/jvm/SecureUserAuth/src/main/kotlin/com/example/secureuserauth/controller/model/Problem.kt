package com.example.secureuserauth.controller.model

import org.springframework.http.ResponseEntity
import java.net.URI

class Problem(
    typeUri: URI
) {
    val type = typeUri.toASCIIString()

    companion object {
        const val MEDIA_TYPE = "application/problem+json"
        fun response(status: Int, problem: Problem) = ResponseEntity
            .status(status)
            .header("Content-Type", MEDIA_TYPE)
            .body<Any>(problem)

        val userAlreadyExists = Problem(
            URI(
                "https://github.com/RaulJCS5/secure-user-auth/tree/main/" +
                    "docs/problems/user-already-exists"
            )
        )
        val insecurePassword = Problem(
            URI(
                "https://github.com/RaulJCS5/secure-user-auth/tree/main/" +
                    "docs/problems/insecure-password"
            )
        )

        val userOrPasswordAreInvalid = Problem(
            URI(
                "https://github.com/RaulJCS5/secure-user-auth/tree/main/" +
                    "docs/problems/user-or-password-are-invalid"
            )
        )

        val invalidRequestContent = Problem(
            URI(
                "https://github.com/RaulJCS5/secure-user-auth/tree/main/" +
                    "docs/problems/invalid-request-content"
            )
        )

        val userNotFound = Problem(
            URI(
                "https://github.com/RaulJCS5/secure-user-auth/tree/main/" +
                        "docs/problems/invalid-request-content"
            )
        )
    }
}