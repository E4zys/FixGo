package com.example.core.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.core.component.navbarcomponent.FabMiddle

@Composable
fun MainScaffold(
    modifier: Modifier = Modifier,
    isHomeSelected: Boolean = false,
    isAktivitasSelected: Boolean = false,
    isRiwayatSelected: Boolean = false,
    isAkunSelected: Boolean = false,
    onHomeClick: () -> Unit = {},
    onAktivitasClick: () -> Unit = {},
    onRiwayatClick: () -> Unit = {},
    onAkunClick: () -> Unit = {},
    content: @Composable (modifier: Modifier) -> Unit
) {
    Scaffold(
        floatingActionButton = { FabMiddle(onClick = {}) },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            NavBar(
                isHomeSelected = isHomeSelected,
                isAktivitasSelected = isAktivitasSelected,
                isRiwayatSelected = isRiwayatSelected,
                isAkunSelected = isAkunSelected,
                onHomeClick = onHomeClick,
                onSearchClick = onAktivitasClick,
                onNotifClick = onRiwayatClick,
                onProfileClick = onAkunClick,
            )
        }
    ) {
        content(modifier.padding(it))
    }
}