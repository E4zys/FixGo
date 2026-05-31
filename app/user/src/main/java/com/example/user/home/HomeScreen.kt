package com.example.user.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.core.component.Carousel

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column {
        Carousel()
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("🏠 Beranda")
        }
    }
}
