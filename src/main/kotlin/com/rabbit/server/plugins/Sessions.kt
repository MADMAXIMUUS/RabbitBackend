package com.rabbit.server.plugins

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import java.io.File

fun Application.configureSessions() {
    install(Sessions) {
        header<UserSession>("session_id", directorySessionStorage(File("build/.sessions")))
    }
}