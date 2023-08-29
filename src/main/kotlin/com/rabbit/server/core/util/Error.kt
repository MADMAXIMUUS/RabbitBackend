package com.rabbit.server.core.util

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val errorCode: Int,
    val errorMessage: String
)

enum class Error(val errorMessage: String) {
    DATABASE_ERROR("something_wrong"),
    PASSWORD_NOT_MATCH_ERROR("password_not_match"),
    USER_NOT_FOUND("user_not_found"),
    SAVE_FILE_ERROR("save_file_error"),
}