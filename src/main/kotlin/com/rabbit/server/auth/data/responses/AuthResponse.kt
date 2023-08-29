package com.rabbit.server.auth.data.responses

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerializedName("session_id") val sessionId: String
)
