package com.example.secureuserauth.repository.jdbi

import org.jdbi.v3.core.Handle
import com.example.secureuserauth.repository.Transaction
import com.example.secureuserauth.repository.UsersRepository

class JdbiTransaction(
    private val handle: Handle
) : Transaction {

    override val usersRepository: UsersRepository by lazy { JdbiUsersRepository(handle) }

    override fun rollback() {
        handle.rollback()
    }
}