package com.rabbit.server.auth.util

import java.time.LocalDate

fun String.toLocalDate(): LocalDate{
    return LocalDate.parse(this)
}