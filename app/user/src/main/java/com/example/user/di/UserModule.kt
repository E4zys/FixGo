package com.example.user.di

import com.example.user.auth.data.datasource.UserDataSource
import com.example.user.auth.data.datasource.UserDataSourceImpl
import com.example.user.auth.data.repository.UserRepositoryImpl
import com.example.user.auth.domain.repository.UserRepository
import com.example.user.auth.domain.usecases.LoginUserUseCase
import com.example.user.auth.domain.usecases.RegisterUserUseCase
import com.example.user.auth.presentation.viewmodel.DaftarViewModel
import com.example.user.auth.presentation.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val userModule = module {
    // Data Source
    single<UserDataSource> { UserDataSourceImpl(get()) }

    // Repository
    single<UserRepository> { UserRepositoryImpl(get()) }

    // UseCases
    single { RegisterUserUseCase(get()) }
    single { LoginUserUseCase(get()) }

    // ViewModels
    viewModelOf(::DaftarViewModel)
    viewModelOf(::LoginViewModel)
}
