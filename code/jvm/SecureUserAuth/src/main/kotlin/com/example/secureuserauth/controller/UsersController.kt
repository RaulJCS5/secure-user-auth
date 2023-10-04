package com.example.secureuserauth.controller

import com.example.secureuserauth.controller.model.*
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.secureuserauth.utils.Either
import com.example.secureuserauth.domain.User
import com.example.secureuserauth.service.UsersService
import java.lang.System.currentTimeMillis
import java.util.*

@RestController
class UsersController(
    private val usersService: UsersService
) {
    @PostMapping(Uris.Users.CREATE)
    fun create(@RequestBody input: UserCreateInputModel): ResponseEntity<*> {
        val res = usersService.createUser(input.username, input.password)
        return when (res) {
            is Either.Right -> ResponseEntity.status(201)
                .header(
                    "Location",
                    Uris.Users.byId(res.value).toASCIIString()
                ).build<Unit>()

            is Either.Left -> when (res.value) {
                UserCreationError.InsecurePassword -> Problem.response(400, Problem.insecurePassword)
                UserCreationError.UserAlreadyExists -> Problem.response(400, Problem.userAlreadyExists)
            }
        }
    }
    //@CrossOrigin( origins = ["http://localhost:3000"], allowCredentials = "true")
    @PostMapping(Uris.Users.TOKEN)
    fun token(@RequestBody input: UserCreateTokenInputModel/*, response: HttpServletResponse*/): ResponseEntity<*> {
        val res = usersService.createToken(input.username, input.password)
        return when (res) {
            is Either.Right -> {
                val jsonToken = Jwts.builder()
                    .setSubject(res.value)
                    .signWith(SignatureAlgorithm.HS256, "secret".toByteArray())
                    .setExpiration(Date(currentTimeMillis() + 1000 * 60 * 60 * 10))
                    .compact()
                /*val cookie: Cookie = Cookie("cookieToken", jsonToken)
                cookie.path = "/"
                cookie.isHttpOnly = true
                cookie.maxAge = 7 * 24 * 60 * 60 // expires in 7 days
                cookie.domain = "localhost"
                response.addCookie(cookie)*/
                ResponseEntity.status(200)
                    .body(UserTokenCreateOutputModel(jsonToken))
            }
            is Either.Left -> when (res.value) {
                TokenCreationError.UserOrPasswordAreInvalid -> Problem.response(400, Problem.userOrPasswordAreInvalid)
            }
        }
    }

    @GetMapping(Uris.Users.HOME)
    fun getUserHome(user: User): UserHomeOutputModel {
        return UserHomeOutputModel(
            id = user.id.toString(),
            username = user.username
        )
    }

    @GetMapping(Uris.Users.GET_BY_ID)
    fun getById(@PathVariable id: String): ResponseEntity<*> {
        return when (val res = usersService.getUserById(id)) {
            is Either.Right -> ResponseEntity.status(200)
                .header(
                    "Location",
                    Uris.Users.byId(res.value.id.toString()).toASCIIString()
                ).body(
                    UserHomeOutputModel(
                        id = res.value.id.toString(),
                        username = res.value.username
                    )
                )

            is Either.Left -> when (res.value) {
                UserGetError.UserNotFound -> Problem.response(400, Problem.userNotFound)
            }
        }
    }

    @PostMapping(Uris.Users.SIGN_OUT)
    fun signOut(user: User): ResponseEntity<*> {
        val result = usersService.invalidateToken(user)
        return when (result) {
            true -> ResponseEntity.status(204).build<Unit>() // Token invalidated successfully
            false -> ResponseEntity.status(400).build<Unit>() // Invalid token or other error
        }
    }
}

data class TokenRequestModel(val token: String)
