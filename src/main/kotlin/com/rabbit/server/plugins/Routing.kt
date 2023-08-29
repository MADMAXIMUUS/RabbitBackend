package com.rabbit.server.plugins

import com.rabbit.server.auth.routes.login
import com.rabbit.server.auth.routes.registration
import com.rabbit.server.auth.service.AuthService
import com.rabbit.server.auth.util.EncryptManager
import com.rabbit.server.core.util.Constants.PROFILE_PICTURE_PATH
import com.rabbit.server.profile.service.UserService
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File

fun Application.configureRouting() {
    val authService: AuthService by inject()
    val userService: UserService by inject()

    val hashSecret = environment.config.property("hash.secret").getString()
    val hashAlgorithm = environment.config.property("hash.algorithm").getString()
    val hashIterations = environment.config.property("hash.iterations").getString().toInt()
    val hashKeyLength = environment.config.property("hash.keyLength").getString().toInt()

    val encryptManager = EncryptManager(hashAlgorithm, hashIterations, hashKeyLength, hashSecret)

    routing {
        route("/") {
            get() {
                call.respondText("Hello from Rabbit")
            }
        }
        registration(authService, userService, encryptManager)
        login(authService, encryptManager)

        staticFiles("/profile_pictures", File(PROFILE_PICTURE_PATH))
    }
}
