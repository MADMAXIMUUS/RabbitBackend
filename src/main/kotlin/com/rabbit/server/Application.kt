package com.rabbit.server

import com.rabbit.server.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.moduleCore() {
    DatabaseFactory.init()
    configureSerialization()
    configureI18n()
    configureKoin()
    configureCors()
    configureMonitoring()
    configureSessions()
    configureSecurity()
    configureRouting()
}
