package com.rabbit.server.profile.data.repository

import com.rabbit.server.plugins.dbQuery
import com.rabbit.server.profile.data.models.UserEntity

class UserRepositoryImpl : UserRepository {
    override suspend fun getUserById(userId: Long): UserEntity? {
        return dbQuery {
            UserEntity.findById(userId)
        }
    }

    override suspend fun updateUserAvatar(userId: Long, avatarUrl: String): Boolean {
        dbQuery {
            val user = getUserById(userId) ?: return@dbQuery false
            user.avatar = avatarUrl
        }
        return true
    }

}