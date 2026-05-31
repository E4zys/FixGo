package com.example.user.di

import com.example.user.akun.AkunViewModel
import com.example.user.auth.data.datasource.UserDataSource
import com.example.user.auth.data.datasource.UserDataSourceImpl
import com.example.user.auth.data.repository.UserRepositoryImpl
import com.example.user.auth.domain.repository.UserRepository
import com.example.user.auth.domain.usecases.LoginUserUseCase
import com.example.user.auth.domain.usecases.LogoutUseCase
import com.example.user.auth.domain.usecases.RegisterUserUseCase
import com.example.user.auth.presentation.viewmodel.DaftarViewModel
import com.example.user.auth.presentation.viewmodel.LoginViewModel
import com.example.user.isiprofile.data.datasource.OnboardingDataSource
import com.example.user.isiprofile.data.datasource.OnboardingDataSourceImpl
import com.example.user.isiprofile.data.repository.OnboardingRepositoryImpl
import com.example.user.isiprofile.domain.repository.OnboardingRepository
import com.example.user.isiprofile.domain.usecases.CheckOnboardingStatusUseCase
import com.example.user.isiprofile.domain.usecases.SubmitOnboardingUseCase
import com.example.user.isiprofile.presentation.viewmodel.IsiProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val userModule = module {
    // ── Auth Data Sources ─────────────────────────────────────────────────────
    single<UserDataSource> { UserDataSourceImpl(get()) }

    // ── Onboarding Data Sources ───────────────────────────────────────────────
    single<OnboardingDataSource> { OnboardingDataSourceImpl(get()) }

    // ── Repositories ──────────────────────────────────────────────────────────
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get(), get()) }

    // ── Use Cases ─────────────────────────────────────────────────────────────
    single { RegisterUserUseCase(get()) }
    single { LoginUserUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { CheckOnboardingStatusUseCase(get()) }
    single { SubmitOnboardingUseCase(get()) }

    // ── ViewModels ────────────────────────────────────────────────────────────
    viewModelOf(::DaftarViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::AkunViewModel)
    viewModelOf(::IsiProfileViewModel)
}
