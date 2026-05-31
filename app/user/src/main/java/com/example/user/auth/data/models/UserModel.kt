package com.example.user.auth.data.models

import com.example.user.auth.domain.entities.User
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val email: String,
    val token: String? = null
)

fun UserModel.toDomain(): User {
    return User(
        email = this.email,
        token = this.token
    )
}
