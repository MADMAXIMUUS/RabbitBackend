package com.rabbit.server.plugins


import com.github.aymanizz.ktori18n.I18n
import io.ktor.server.application.*
import java.util.*

fun Application.configureI18n() {
    install(I18n) {
        supportedLocales = listOf("en", "ru").map(Locale::forLanguageTag)
    }
}