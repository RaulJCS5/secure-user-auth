package com.example.secureuserauth.repository

interface TransactionManager {
    fun <R> run(block: (Transaction) -> R): R
}