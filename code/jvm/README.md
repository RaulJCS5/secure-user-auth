# Spring backend

## JWT and Bearer token


JWT (JSON Web Token) as a Bearer token in an HTTP request for authentication and authorization purposes. Using JWTs as Bearer tokens is a widely used method for securing APIs and web applications.

Here's how it works:

Token Creation: When a user logs in or authenticates, the server generates a JWT token that contains information about the user and their permissions. This token is typically signed with a secret key or a private key.

Token Storage: The JWT token can be stored on the client-side securely, often in an HTTP-only cookie, local storage, or a session storage, depending on the security requirements.

Bearer Token in Requests: To authenticate API requests, the client includes the JWT token in the Authorization header of the HTTP request as a Bearer token. The header might look like this:

Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
The token itself follows the "Bearer " prefix.

Server Validation: On the server side, the application receives the token from the Authorization header. It then validates the token's signature and expiration date and checks the user's permissions and identity based on the token's content.

Access Control: The server uses the information in the JWT to determine whether the user is authorized to access the requested resource or perform the requested action.

JWTs provide a secure and efficient way to handle authentication and authorization in web applications and APIs. They are self-contained, meaning they contain all the necessary information, reducing the need for additional database queries to check user permissions. Additionally, since JWTs are signed, they can be used to verify the authenticity of the token.

However, it's crucial to ensure that JWTs are stored securely on the client-side and transmitted over HTTPS to prevent unauthorized access or tampering.
## [Form Login](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html#servlet-authentication-form-custom)
## [Disable CSRF Protection](https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#disable-csrf)

If you need to disable CSRF protection, you can do so using the following configuration:

- Disable CSRF
```kotlin
import org.springframework.security.config.annotation.web.invoke

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            // ...
            csrf {
                disable()
            }
        }
        return http.build()
    }
}
```

[Spring security jump to a wrong url after login success](https://github.com/spring-projects/spring-security/issues/12635)