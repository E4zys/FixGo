package com.example.fixgo.router

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.fixgo.AppViewModel
import com.example.fixgo.AuthState
import com.example.fixgo.ui.main.MainScreen
import com.example.user.auth.presentation.ui.DaftarScreen
import com.example.user.auth.presentation.ui.LoginScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    appViewModel: AppViewModel = koinViewModel()
) {
    val authState by appViewModel.authState.collectAsStateWithLifecycle()

    // ── Auth Guard ────────────────────────────────
    LaunchedEffect(authState) {
        when (authState) {
            AuthState.LoggedIn -> navController.navigate(RootRoute.Main) {
                // Hapus semua backstack Auth agar tidak bisa back ke Login
                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
            }
            AuthState.LoggedOut -> navController.navigate(RootRoute.Auth) {
                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
            }
            AuthState.Loading -> Unit
        }
    }

    if (authState == AuthState.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // ── Nav Graph ───────────────────────────────────────────────────────────
    NavHost(
        navController = navController,
        startDestination = RootRoute.Auth, // Sementara Auth, LaunchedEffect yang redirect
        modifier = modifier
    ) {
        // ── Auth Graph ─────────────────────────────────────────────────────
        navigation<RootRoute.Auth>(startDestination = AuthRoute.Login) {

            composable<AuthRoute.Login> {
                LoginScreen(
                    onNavigateToDaftar = {
                        navController.navigate(AuthRoute.Daftar)
                    }
                )
            }

            composable<AuthRoute.Daftar> {
                DaftarScreen(
                    onNavigateToLogin = {
                        navController.navigate(AuthRoute.Login) {
                            popUpTo(AuthRoute.Login) { inclusive = true }
                        }
                    }
                )
            }
        }

        // ── Main Graph ─────────────────────────────────────────────────────
        composable<RootRoute.Main> {
            MainScreen()
        }
    }
}
