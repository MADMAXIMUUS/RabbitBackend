package com.rabbit.server.auth.service

import com.rabbit.server.auth.data.repository.AuthRepository
import com.rabbit.server.auth.data.request.CreateAccountRequest
import com.rabbit.server.auth.data.request.LoginRequest
import com.rabbit.server.auth.util.EncryptManager
import com.rabbit.server.auth.util.toLocalDate
import com.rabbit.server.core.util.Error
import com.rabbit.server.core.util.Result
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AuthService(
    private val authRepository: AuthRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(AuthService::class.java)

    suspend fun createUser(
        createAccountRequest: CreateAccountRequest,
        encryptManager: EncryptManager
    ): Result<Long> {
        logger.debug("Start of create new user({})", createAccountRequest)
        return try {
            val salt = encryptManager.generateRandomSalt()
            val id = authRepository.createUser(
                createAccountRequest.name,
                createAccountRequest.email,
                createAccountRequest.birthdate.toLocalDate(),
                salt,
                encryptManager.generateHash(salt, createAccountRequest.password)
            )
            Result.Success(id)
        } catch (e: Exception) {
            logger.debug("Something went wrong")
            Result.Failure(Error.DATABASE_ERROR)
        }
    }

    suspend fun checkUserExist(input: String): Boolean = authRepository.checkUserExist(input)

    suspend fun checkPasswordMatch(loginRequest: LoginRequest, encryptManager: EncryptManager): Result<Long> {
        val user = authRepository.getUser(loginRequest.email)?: return Result.Failure(Error.USER_NOT_FOUND)
        val salt = user.passwordSalt
        val password = user.passwordHash
        val inputPassword = encryptManager.generateHash(salt, loginRequest.password)
        return if (password == inputPassword) Result.Success(user.id.value)
        else Result.Failure(Error.PASSWORD_NOT_MATCH_ERROR)
    }

}