package com.example.user.auth.domain.entities

data class User(
    val email: String = "",
    val token: String? = null
)
