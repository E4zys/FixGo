package com.example.user.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.component.Validators
import com.example.core.model.BaseState
import com.example.user.auth.domain.entities.UserPayload
import com.example.user.auth.domain.usecases.RegisterUserUseCase
import com.example.user.auth.presentation.state.DaftarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DaftarViewModel(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DaftarState())
    val state = _state.asStateFlow()

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email, isFormValid = validateForm(email, it.password, it.rePassword)) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password, isFormValid = validateForm(it.email, password, it.rePassword)) }
    }

    fun onRePasswordChange(rePassword: String) {
        _state.update { it.copy(rePassword = rePassword, isFormValid = validateForm(it.email, it.password, rePassword)) }
    }

    private fun validateForm(email: String, password: String, rePassword: String): Boolean {
        return Validators.validEmail(email).isValid &&
                Validators.notBlank(password).isValid &&
                Validators.minLength(8)(password).isValid &&
                Validators.matchWith(password)(rePassword).isValid
    }

    fun register() {
        val current = _state.value
        _state.update { it.copy(status = BaseState.Loading) }

        viewModelScope.launch {
            val payload = UserPayload(
                email = current.email,
                password = current.password
            )
            registerUserUseCase(payload).fold(
                onSuccess = { user ->
                    _state.update { it.copy(status = BaseState.Success) }
                },
                onFailure = { error ->
                    _state.update { it.copy(status = BaseState.Error(error.message ?: "Pendaftaran gagal.")) }
                }
            )
        }
    }

    fun clearSnackbarState() {
        _state.update { it.copy(status = BaseState.Idle) }
    }
}