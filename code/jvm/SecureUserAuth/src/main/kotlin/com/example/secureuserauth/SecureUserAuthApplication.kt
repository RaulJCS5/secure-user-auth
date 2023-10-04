package com.example.secureuserauth

import com.example.secureuserauth.config.secureUserAuthConfigure
import com.example.secureuserauth.utils.Clock
import com.example.secureuserauth.utils.Sha256TokenEncoder
import org.jdbi.v3.core.Jdbi
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.Instant

@SpringBootApplication
class SecureUserAuthApplication{
	@Bean
	fun clock() = object : Clock {
		override fun now() = Instant.now()
	}
	@Bean
	fun passwordEncoder() = BCryptPasswordEncoder()
	@Bean
	fun tokenEncoder() = Sha256TokenEncoder()
	@Bean
	fun jdbi() = Jdbi.create(
		PGSimpleDataSource().apply {
			setURL("jdbc:postgresql://localhost:5432/postgres?user=postgres&password=postgres")
		}).secureUserAuthConfigure()
}

fun main(args: Array<String>) {
	runApplication<SecureUserAuthApplication>(*args)
}