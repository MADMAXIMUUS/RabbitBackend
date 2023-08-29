package com.rabbit.server.plugins

import com.rabbit.server.core.util.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
    install(Authentication) {
        session<UserSession>("session_auth") {
            validate { session: UserSession ->
                session
            }
            challenge {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse(HttpStatusCode.Unauthorized.value, "")
                )
            }
        }
    }
}

data class UserSession(
    val userId: Long,
    val ip: String,
    val userAgent: String
) : Principal