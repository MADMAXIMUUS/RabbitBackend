package com.rabbit.server.plugins

import com.rabbit.server.di.authModule
import com.rabbit.server.di.userModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

fun Application.configureKoin() {
    install(Koin) {
        SLF4JLogger()
        modules(
            authModule,
            userModule
        )
    }
}