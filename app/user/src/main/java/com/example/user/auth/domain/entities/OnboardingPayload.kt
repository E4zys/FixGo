package com.example.user.auth.domain.entities

data class OnboardingPayload(
    val username: String,
    val phone: String,
    val jenisKendaraan: String,
    val merkKendaraan: String,
    val tipeKendaraan: String,
    val tahunKendaraan: Int,
    val totalKm: Int
)
