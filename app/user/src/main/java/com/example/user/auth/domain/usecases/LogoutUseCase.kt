package com.example.user.auth.domain.usecases

import com.example.user.auth.domain.repository.UserRepository

class LogoutUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.logout()
}
