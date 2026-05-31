package com.example.fixgo.router

import kotlinx.serialization.Serializable

@Serializable
sealed class MainRoute {
    @Serializable data object Home : MainRoute()
    @Serializable data object Aktivitas : MainRoute()
    @Serializable data object Riwayat : MainRoute()
    @Serializable data object Akun : MainRoute()
}