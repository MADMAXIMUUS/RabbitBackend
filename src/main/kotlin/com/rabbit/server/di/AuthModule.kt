package com.rabbit.server.di

import com.rabbit.server.auth.data.repository.AuthRepository
import com.rabbit.server.auth.data.repository.AuthRepositoryImpl
import com.rabbit.server.auth.service.AuthService
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl()
    }
    single { AuthService(get()) }
}