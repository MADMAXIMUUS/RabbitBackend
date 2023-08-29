package com.rabbit.server.di

import com.rabbit.server.profile.data.repository.UserRepository
import com.rabbit.server.profile.data.repository.UserRepositoryImpl
import com.rabbit.server.profile.service.UserService
import org.koin.dsl.module

val userModule = module {
    single<UserRepository> {
        UserRepositoryImpl()
    }
    single { UserService(get()) }
}