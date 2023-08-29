package com.rabbit.server.auth.data.repository

import com.rabbit.server.plugins.dbQuery
import com.rabbit.server.profile.data.models.UserEntity
import com.rabbit.server.profile.data.models.UserTable
import org.jetbrains.exposed.sql.or
import java.time.LocalDate

class AuthRepositoryImpl : AuthRepository {
    override suspend fun createUser(
        name: String,
        email: String,
        birthdate: LocalDate,
        salt: String,
        passwordHash: String
    ): Long {
        return dbQuery {
            UserEntity.new {
                this.name = name
                this.email = email
                this.birthdate = birthdate
                this.passwordSalt = salt
                this.passwordHash = passwordHash
            }.id.value
        }
    }

    override suspend fun checkUserExist(input: String): Boolean {
        dbQuery {
            UserEntity.find { UserTable.email eq input or (UserTable.username eq input) }.firstOrNull()
        } ?: return false
        return true
    }

    override suspend fun getUser(input: String): UserEntity? {
        return dbQuery {
            UserEntity.find { UserTable.email eq input or (UserTable.username eq input) }.firstOrNull()
        }
    }
}