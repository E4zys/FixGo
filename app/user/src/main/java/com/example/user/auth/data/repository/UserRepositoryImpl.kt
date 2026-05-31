package com.example.user.auth.data.repository

import com.example.user.auth.data.datasource.UserDataSource
import com.example.user.auth.data.models.toDomain
import com.example.user.auth.data.models.toModel
import com.example.user.auth.domain.entities.User
import com.example.user.auth.domain.entities.UserPayload
import com.example.user.auth.domain.repository.UserRepository

class UserRepositoryImpl(private val dataSource: UserDataSource) : UserRepository {

    override suspend fun registerUser(payload: UserPayload): Result<User> = runCatching {
        val payloadModel = payload.toModel()
        val registeredModel = dataSource.registerUser(payloadModel)
        registeredModel.toDomain()
    }

    override suspend fun loginUser(payload: UserPayload): Result<User> = runCatching {
        val payloadModel = payload.toModel()
        val loggedInModel = dataSource.loginUser(payloadModel)
        loggedInModel.toDomain()
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        dataSource.logout()
    }
}
