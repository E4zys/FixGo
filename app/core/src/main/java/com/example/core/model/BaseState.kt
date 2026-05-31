package com.example.core.model

sealed class BaseState {
    object Idle: BaseState()
    object Loading : BaseState()
    object Success : BaseState()
    data class Error(val message: String) : BaseState()
}

