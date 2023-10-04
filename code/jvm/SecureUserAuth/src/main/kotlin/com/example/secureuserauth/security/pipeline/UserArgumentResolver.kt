package com.example.secureuserauth.security.pipeline

import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import com.example.secureuserauth.domain.User

// QUESTION: why is this annotated with @Component
// ANSWER: because it is a bean that is used by Spring to resolve method arguments in controllers (see PipelineConfigurer)
@Component
class UserArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == User::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)
            ?: throw IllegalStateException("Invalid request type.")
        return getUserFrom(request) ?: throw IllegalStateException("User not found.")
    }

    companion object {
        private val KEY: String = "UserArgumentResolver"

        fun addUserTo(user: User, request: HttpServletRequest) {
            return request.setAttribute(KEY, user)
        }
        fun getUserFrom(request: HttpServletRequest): User? {
            return request.getAttribute(KEY)?.let {
                it as? User
            }
        }
    }

}
