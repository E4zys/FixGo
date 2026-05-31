package com.example.user.auth.presentation.state

import com.example.core.model.BaseState

data class DaftarState(
    val email: String = "",
    val password: String = "",
    val rePassword: String = "",
    val isFormValid: Boolean = false,
    val status: BaseState = BaseState.Idle
)