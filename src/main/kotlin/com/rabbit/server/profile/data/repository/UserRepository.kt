package com.rabbit.server.profile.data.repository

import com.rabbit.server.profile.data.models.UserEntity

interface UserRepository {

    suspend fun getUserById(userId: Long): UserEntity?
    suspend fun updateUserAvatar(userId: Long, avatarUrl: String): Boolean

}