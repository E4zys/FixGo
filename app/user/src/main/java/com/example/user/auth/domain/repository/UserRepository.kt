package com.example.user.auth.domain.repository

import com.example.user.auth.domain.entities.User
import com.example.user.auth.domain.entities.UserPayload

interface UserRepository {
    suspend fun registerUser(payload: UserPayload): Result<User>
    suspend fun loginUser(payload: UserPayload): Result<User>
}
