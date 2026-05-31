package com.example.fixgo.router

import kotlinx.serialization.Serializable

@Serializable
sealed class RootRoute {
    @Serializable data object Auth : RootRoute()
    @Serializable data object Main : RootRoute()
    @Serializable data object IsiProfile : RootRoute()
}
