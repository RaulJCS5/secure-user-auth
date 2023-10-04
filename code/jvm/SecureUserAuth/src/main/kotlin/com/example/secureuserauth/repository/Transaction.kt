package com.example.secureuserauth.repository

interface Transaction {

    val usersRepository: UsersRepository

    fun rollback()
}