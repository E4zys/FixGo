package com.example.user.isiprofile.presentation.state

import com.example.core.model.BaseState

data class IsiProfileState(
    // Page 1: User Profile
    val nama: String = "",
    val phone: String = "",
    
    // Page 2: Kendaraan
    val jenisKendaraan: String = "motor", // "motor" or "mobil"
    val merkKendaraan: String = "",
    val tipeKendaraan: String = "",
    val tahunKendaraan: String = "",
    val totalKm: String = "",
    
    val isPage1Valid: Boolean = false,
    val isPage2Valid: Boolean = false,
    val submitAttempted: Boolean = false,

    val submitState: BaseState = BaseState.Idle
)
