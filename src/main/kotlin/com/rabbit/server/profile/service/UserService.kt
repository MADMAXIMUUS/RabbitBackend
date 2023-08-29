package com.rabbit.server.profile.service

import com.rabbit.server.core.util.Error
import com.rabbit.server.core.util.Result
import com.rabbit.server.profile.data.models.UserEntity
import com.rabbit.server.profile.data.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class UserService(
    private val userRepository: UserRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)

    suspend fun getUser(userId: Long): com.rabbit.server.core.util.Result<UserEntity> {
        val user = userRepository.getUserById(userId) ?: return Result.Failure(
            Error.USER_NOT_FOUND
        )
        return Result.Success(user)
    }

    suspend fun updateAvatar(userId: Long, avatarUrl: String): Result<Unit> {
        return if (userRepository.updateUserAvatar(userId, avatarUrl)) Result.Success(Unit)
        else Result.Failure(Error.USER_NOT_FOUND)
    }
}