package com.rabbit.server.auth.data.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateAccountRequest(
    val email: String,
    val name: String,
    val birthdate: String,
    val password: String
)