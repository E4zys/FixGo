package com.example.user.auth.data.datasource

import com.example.user.auth.data.models.UserModel
import com.example.user.auth.data.models.UserPayloadModel

interface UserDataSource {
    suspend fun registerUser(payload: UserPayloadModel): UserModel
    suspend fun loginUser(payload: UserPayloadModel): UserModel
    suspend fun logout()
}
