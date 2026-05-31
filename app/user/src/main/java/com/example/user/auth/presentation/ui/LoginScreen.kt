package com.example.user.auth.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.component.Button
import com.example.core.component.Validators
import com.example.core.component.field.FillTextField
import com.example.core.component.field.FillPasswordField
import com.example.core.model.BaseState
import com.example.user.auth.presentation.components.BaseAuthLayout
import com.example.user.auth.presentation.viewmodel.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigateToDaftar: () -> Unit = {},
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Snackbar listener — menggunakan BaseState
    LaunchedEffect(state.status) {
        when (val status = state.status) {
            is BaseState.Error -> {
                snackbarHostState.showSnackbar(status.message)
                viewModel.clearSnackbarState()
            }
            is BaseState.Success -> {
                snackbarHostState.showSnackbar("Login berhasil! Selamat datang.")
                viewModel.clearSnackbarState()
            }
            else -> {}
        }
    }

    BaseAuthLayout(
        title = "Masuk ke FixGo",
        subtitle = "Pantau kesehatan kendaraan dan temukan bengkel terdekat dengan cepat.",
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        formContent = {
            FillTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = "Email",
                placeholder = "Masukkan email",
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon", tint = Color.Gray) },
                validators = listOf(Validators.validEmail)
            )

            FillPasswordField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                label = "Password",
                placeholder = "Masukkan password",
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon", tint = Color.Gray) },
                validators = listOf(Validators.notBlank, Validators.minLength(8))
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.login() },
                enabled = state.isFormValid && state.status !is BaseState.Loading
            ) {
                Text(if (state.status is BaseState.Loading) "Memuat..." else "Masuk")
            }
        },
        bottomLinkContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Belum punya akun ?",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Daftar sekarang",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onNavigateToDaftar() }
                )
            }
        }
    )
}
