package com.example.core.component.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CustomSnackbar(
    snackbarMessage: SnackbarMessage?,
    modifier: Modifier = Modifier
) {
    if (snackbarMessage == null) return

    val (backgroundColor, contentColor, icon) = when (snackbarMessage.type) {
        SnackbarType.SUCCESS -> Triple(Color(0xFF4CAF50), Color.White, Icons.Default.CheckCircle)
        SnackbarType.DANGER -> Triple(Color(0xFFF44336), Color.White, Icons.Default.Warning)
        SnackbarType.WARNING -> Triple(Color(0xFFFF9800), Color.White, Icons.Default.Warning)
        SnackbarType.INFO -> Triple(Color(0xFF2196F3), Color.White, Icons.Default.Info)
    }

    Box(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = snackbarMessage.message,
                color = contentColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// Komponen Overlay/Host yang ditempatkan di root untuk memantau manager
@Composable
fun AppSnackbarHost(
    snackbarHostState: SnackbarHostState,
    currentMessage: SnackbarMessage?,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier,
        snackbar = {
            CustomSnackbar(snackbarMessage = currentMessage)
        }
    )
}

