package com.example.secureuserauth.security.pipeline

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import com.example.secureuserauth.domain.User
import com.example.secureuserauth.controller.Uris
import com.example.secureuserauth.service.UsersService

@Component
class AuthenticationInterceptor(
    val usersService: UsersService
) : HandlerInterceptor {

    fun process(authorizationValue: String?): User? {
        if (authorizationValue == null) {
            return null
        }
        val parts = authorizationValue.trim().split(" ")
        if (parts.size != 2) {
            return null
        }
        if (parts[0].lowercase() != SCHEME) {
            return null
        }
        return usersService.getUserByToken(parts[1])
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod) {
            if (!isUserCreationRequest(request)) {
                // enforce authentication
                val user = process(request.getHeader(NAME_AUTHORIZATION_HEADER))
                if (user == null) {
                    response.status = 401
                    response.addHeader(NAME_WWW_AUTHENTICATE_HEADER, SCHEME)
                    return false
                } else {
                    UserArgumentResolver.addUserTo(user, request)
                    return true
                }
            }
        }
        return true
    }
    private fun isUserCreationRequest(request: HttpServletRequest): Boolean {
        // Check if this is a user creation request based on your application's criteria
        // For example, check the request URI or HTTP method
        val requestURI = request.requestURI
        val httpMethod = request.method

        // Define your criteria for user creation here
        // For example, if user creation is allowed for POST requests to "/create-user" endpoint
        return httpMethod == "POST" && (requestURI == Uris.Users.CREATE || requestURI == Uris.Users.TOKEN)
    }
    companion object{
        private const val NAME_AUTHORIZATION_HEADER = "Authorization"
        private const val NAME_WWW_AUTHENTICATE_HEADER = "WWW-Authenticate"
        private const val SCHEME = "bearer"
    }
}
