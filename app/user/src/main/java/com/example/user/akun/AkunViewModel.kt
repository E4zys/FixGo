package com.example.user.akun

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.model.BaseState
import com.example.user.auth.domain.usecases.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AkunViewModel(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _logoutState = MutableStateFlow<BaseState>(BaseState.Idle)
    val logoutState = _logoutState.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            _logoutState.update { BaseState.Loading }
            logoutUseCase().fold(
                onSuccess = { _logoutState.update { BaseState.Success } },
                onFailure = { e -> _logoutState.update { BaseState.Error(e.message ?: "Logout gagal") } }
            )
        }
    }

    fun clearState() {
        _logoutState.update { BaseState.Idle }
    }
}
