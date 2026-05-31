package com.example.user.auth.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KendaraanModel(
    @SerialName("jenis")
    val jenis: String,
    @SerialName("merk")
    val merk: String,
    @SerialName("tipe")
    val tipe: String,
    @SerialName("tahun")
    val tahun: Int,
    @SerialName("total_km")
    val totalKm: Int,
    @SerialName("user_id")
    val userId: String
)
