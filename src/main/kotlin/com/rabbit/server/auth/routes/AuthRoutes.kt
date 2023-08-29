package com.rabbit.server.auth.routes

import com.github.aymanizz.ktori18n.R
import com.github.aymanizz.ktori18n.t
import com.rabbit.server.auth.data.request.CreateAccountRequest
import com.rabbit.server.auth.data.request.LoginRequest
import com.rabbit.server.auth.service.AuthService
import com.rabbit.server.auth.util.EncryptManager
import com.rabbit.server.core.util.Constants
import com.rabbit.server.core.util.Constants.PROFILE_PICTURE_PATH
import com.rabbit.server.core.util.ErrorResponse
import com.rabbit.server.core.util.save
import com.rabbit.server.plugins.UserSession
import com.rabbit.server.profile.service.UserService
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import java.io.File

fun Route.registration(
    authService: AuthService,
    userService: UserService,
    encryptManager: EncryptManager
) {
    post("/api/v1/auth/sign-up") {
        val request = call.receiveNullable<CreateAccountRequest>() ?: kotlin.run {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    errorCode = HttpStatusCode.BadRequest.value,
                    errorMessage = call.t(R("invalid_request"))
                )
            )
            return@post
        }
        if (authService.checkUserExist(request.email)) {
            call.respond(
                HttpStatusCode.Conflict,
                ErrorResponse(
                    errorCode = HttpStatusCode.Conflict.value,
                    errorMessage = call.t(R("user_exist"))
                )
            )
            return@post
        }
        when (val result = authService.createUser(request, encryptManager)) {
            is com.rabbit.server.core.util.Result.Success -> {
                val ipAddress = call.request.origin.remoteAddress
                val userAgent = call.request.userAgent() ?: return@post

                call.sessions.set(UserSession(result.value, ipAddress, userAgent))

                call.respond(HttpStatusCode.OK)
            }

            is com.rabbit.server.core.util.Result.Failure -> {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        errorCode = HttpStatusCode.InternalServerError.value,
                        errorMessage = call.t(R(result.error.errorMessage))
                    )
                )
            }
        }
    }
    authenticate("session_auth") {
        post("/api/v1/auth/add-avatar") {
            val multipart = call.receiveMultipart()
            var fileName: String? = null
            try {
                multipart.forEachPart { partData ->
                    when (partData) {
                        is PartData.FormItem -> Unit

                        is PartData.FileItem -> {
                            fileName = partData.save(PROFILE_PICTURE_PATH)
                        }

                        is PartData.BinaryItem -> Unit
                        is PartData.BinaryChannelItem -> Unit
                    }
                }

                val imageUrl = "${Constants.BASE_URL}profile_pictures/$fileName"
                val userId = call.sessions.get<UserSession>()?.userId ?: kotlin.run {
                    File("${PROFILE_PICTURE_PATH}/$fileName").delete()
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        ErrorResponse(
                            errorCode = HttpStatusCode.Unauthorized.value,
                            errorMessage = ""/*call.t(R("invalid_request"))*/
                        )
                    )
                    return@post
                }
                userService.updateAvatar(userId, imageUrl)
                call.respond(HttpStatusCode.OK)
            } catch (ex: Exception) {
                File("${PROFILE_PICTURE_PATH}/$fileName").delete()
                call.respond(HttpStatusCode.InternalServerError, "Error")
            }
        }
    }
}

fun Route.login(
    authService: AuthService,
    encryptManager: EncryptManager
) {
    post("/api/v1/auth/sign-in") {
        val request = call.receiveNullable<LoginRequest>() ?: kotlin.run {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    errorCode = HttpStatusCode.BadRequest.value,
                    errorMessage = call.t(R("invalid_request"))
                )
            )
            return@post
        }
        if (!authService.checkUserExist(request.email)) {
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(
                    errorCode = HttpStatusCode.NotFound.value,
                    errorMessage = call.t(R("user_not_found"))
                )
            )
            return@post
        }
        when (val result = authService.checkPasswordMatch(request, encryptManager)) {
            is com.rabbit.server.core.util.Result.Success -> {
                val ipAddress = call.request.origin.remoteAddress
                val userAgent = call.request.userAgent() ?: return@post

                call.sessions.set(UserSession(result.value, ipAddress, userAgent))

                call.respond(HttpStatusCode.OK)
            }

            is com.rabbit.server.core.util.Result.Failure -> {
                call.respond(
                    HttpStatusCode.Conflict,
                    ErrorResponse(
                        errorCode = HttpStatusCode.Conflict.value,
                        errorMessage = call.t(R(result.error.errorMessage))
                    )
                )
            }
        }
    }
}