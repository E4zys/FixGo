package com.example.user.auth.domain.usecases

import com.example.user.auth.domain.entities.User
import com.example.user.auth.domain.entities.UserPayload
import com.example.user.auth.domain.repository.UserRepository

class LoginUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(payload: UserPayload): Result<User> {
        return repository.loginUser(payload)
    }
}
