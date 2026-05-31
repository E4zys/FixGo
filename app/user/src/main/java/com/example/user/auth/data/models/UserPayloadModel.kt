package com.example.user.auth.data.models

import com.example.user.auth.domain.entities.UserPayload
import kotlinx.serialization.Serializable

@Serializable
data class UserPayloadModel(
    val email: String,
    val password: String
)

fun UserPayload.toModel(): UserPayloadModel {
    return UserPayloadModel(
        email = this.email,
        password = this.password
    )
}
