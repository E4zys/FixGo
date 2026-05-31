package com.example.fixgo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class AuthState {
    data object Loading : AuthState()
    data object LoggedIn : AuthState()
    data object LoggedOut : AuthState()
}

class AppViewModel(supabaseClient: SupabaseClient) : ViewModel() {

    val authState: StateFlow<AuthState> = supabaseClient.auth.sessionStatus
        .map { status -> 
            when (status) {
                is SessionStatus.Authenticated -> AuthState.LoggedIn
                is SessionStatus.Initializing -> AuthState.Loading
                is SessionStatus.NotAuthenticated -> AuthState.LoggedOut
                else -> AuthState.LoggedOut
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AuthState.Loading
        )
}
