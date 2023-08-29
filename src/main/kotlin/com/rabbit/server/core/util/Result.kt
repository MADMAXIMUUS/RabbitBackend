package com.rabbit.server.core.util

sealed class Result<out T> {
    data class Failure(val error: Error) : Result<Nothing>()
    data class Success<T>(val value: T) : Result<T>()
}