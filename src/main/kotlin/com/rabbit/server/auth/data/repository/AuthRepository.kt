package com.rabbit.server.auth.data.repository

import com.rabbit.server.profile.data.models.UserEntity
import java.time.LocalDate

interface AuthRepository {

    suspend fun createUser(
        name: String,
        email: String,
        birthdate: LocalDate,
        salt: String,
        passwordHash: String
    ): Long

    suspend fun checkUserExist(input: String): Boolean

    suspend fun getUser(input: String): UserEntity?

}