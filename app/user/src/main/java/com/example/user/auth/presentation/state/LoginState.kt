package com.example.user.auth.presentation.state

import com.example.core.model.BaseState

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isFormValid: Boolean = false,
    val status: BaseState = BaseState.Idle
)
