package com.example.secureuserauth.config.mappers

import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext
import com.example.secureuserauth.domain.TokenValidationInfo
import java.sql.ResultSet
import java.sql.SQLException

class TokenValidationInfoMapper : ColumnMapper<TokenValidationInfo> {
    @Throws(SQLException::class)
    override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext?): TokenValidationInfo {
        return TokenValidationInfo(r.getString(columnNumber))
    }
}