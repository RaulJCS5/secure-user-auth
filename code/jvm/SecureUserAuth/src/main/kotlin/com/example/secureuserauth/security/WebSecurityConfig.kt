package com.example.secureuserauth.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
@EnableWebMvc
class WebSecurityConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**") // Specifies which URL paths an annotated controller class is mapped to.
            .allowedOrigins("http://localhost:3000") // Specifies which origins are allowed to access the resources.
            .allowedMethods("GET", "POST", "PUT", "DELETE") // Specifies which methods are allowed to access the resources.
            .allowCredentials(true) // Specifies whether the browser should include any cookies associated with the domain of the request being annotated.
    }
}