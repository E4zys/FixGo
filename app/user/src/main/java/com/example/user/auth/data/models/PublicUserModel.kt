package com.example.user.auth.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PublicUserModel(
    @SerialName("id")
    val id: String,
    @SerialName("username")
    val username: String? = null,
    @SerialName("email")
    val email: String,
    @SerialName("phone")
    val phone: String? = null
)
