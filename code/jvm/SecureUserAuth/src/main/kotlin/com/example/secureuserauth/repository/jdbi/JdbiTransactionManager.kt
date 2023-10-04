package com.example.secureuserauth.repository.jdbi

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import com.example.secureuserauth.repository.Transaction
import com.example.secureuserauth.repository.TransactionManager

@Component
class JdbiTransactionManager(
    private val jdbi: Jdbi
) : TransactionManager {

    override fun <R> run(block: (Transaction) -> R): R =
        jdbi.inTransaction<R, Exception> { handle ->
            val transaction = JdbiTransaction(handle)
            block(transaction)
        }
}