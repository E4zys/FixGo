package com.example.fixgo.router

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthRoute {
    @Serializable data object Login : AuthRoute()
    @Serializable data object Daftar : AuthRoute()
}