package com.example.fixgo.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core.component.MainScaffold
import com.example.fixgo.router.MainRoute
import com.example.user.aktivitas.AktivitasContent
import com.example.user.akun.AkunContent
import com.example.user.home.HomeScreen
import com.example.user.riwayat.RiwayatContent

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination

    val isHome = destination?.hasRoute<MainRoute.Home>() == true
    val isAktivitas = destination?.hasRoute<MainRoute.Aktivitas>() == true
    val isRiwayat = destination?.hasRoute<MainRoute.Riwayat>() == true
    val isAkun = destination?.hasRoute<MainRoute.Akun>() == true

    MainScaffold(
        isHomeSelected = isHome,
        isAktivitasSelected = isAktivitas,
        isRiwayatSelected = isRiwayat,
        isAkunSelected = isAkun,
        onHomeClick = {
            navController.navigate(MainRoute.Home) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
        onAktivitasClick = {
            navController.navigate(MainRoute.Aktivitas) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
        onRiwayatClick = {
            navController.navigate(MainRoute.Riwayat) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
        onAkunClick = {
            navController.navigate(MainRoute.Akun) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
    ) { modifier ->
        NavHost(
            navController = navController,
            startDestination = MainRoute.Home,
            modifier = modifier
        ) {
            composable<MainRoute.Home> { HomeScreen() }
            composable<MainRoute.Aktivitas> { AktivitasContent() }
            composable<MainRoute.Riwayat> { RiwayatContent() }
            composable<MainRoute.Akun> { AkunContent() }
        }
    }
}


