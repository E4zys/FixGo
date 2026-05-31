package com.example.user.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.component.Validators
import com.example.core.model.BaseState
import com.example.user.auth.domain.entities.UserPayload
import com.example.user.auth.domain.usecases.LoginUserUseCase
import com.example.user.auth.presentation.state.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    // ─── Field handlers ───────────────────────────────────────────────────────

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email, isFormValid = validateForm(email, it.password)) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password, isFormValid = validateForm(it.email, password)) }
    }

    // ─── Validation ───────────────────────────────────────────────────────────

    private fun validateForm(email: String, password: String): Boolean {
        return Validators.validEmail(email).isValid &&
                Validators.notBlank(password).isValid &&
                Validators.minLength(8)(password).isValid
    }

    // ─── Submit ───────────────────────────────────────────────────────────────

    fun login() {
        val current = _state.value
        _state.update { it.copy(status = BaseState.Loading) }

        viewModelScope.launch {
            val payload = UserPayload(email = current.email, password = current.password)
            loginUserUseCase(payload).fold(
                onSuccess = { user ->
                    _state.update { it.copy(status = BaseState.Success) }
                },
                onFailure = { error ->
                    _state.update { it.copy(status = BaseState.Error(error.message ?: "Login gagal.")) }
                }
            )
        }
    }

    fun clearSnackbarState() {
        _state.update { it.copy(status = BaseState.Idle) }
    }
}
