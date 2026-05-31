package com.example.core.component.snackbar

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

enum class SnackbarType {
    SUCCESS, DANGER, WARNING, INFO
}

data class SnackbarMessage(
    val message: String,
    val type: SnackbarType = SnackbarType.INFO
)

object SnackbarManager {
    private val _messages = MutableSharedFlow<SnackbarMessage>()
    val messages = _messages.asSharedFlow()

    suspend fun showMessage(message: String, type: SnackbarType = SnackbarType.INFO) {
        _messages.emit(SnackbarMessage(message, type))
    }
}

/**
 * Composable helper: collect pesan dari [SnackbarManager] dan tampilkan via [snackbarHostState].
 * Taruh di dalam Scaffold scope, setelah SnackbarHost didefinisikan.
 *
 * Contoh pemakaian:
 * ```kotlin
 * val snackbarHostState = remember { SnackbarHostState() }
 * Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { ... }
 * ObserveSnackbar(snackbarHostState)
 * ```
 */
@Composable
fun ObserveSnackbar(snackbarHostState: SnackbarHostState) {
    LaunchedEffect(Unit) {
        SnackbarManager.messages.collect { msg ->
            snackbarHostState.showSnackbar(msg.message)
        }
    }
}
