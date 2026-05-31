package com.example.core.component.field

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.core.component.FieldValidator

/**
 * TextField dengan validasi real-time bawaan.
 *
 * Contoh pemakaian:
 * ```kotlin
 * CustomTextField(
 *     value = state.email,
 *     onValueChange = viewModel::onEmailChange,
 *     label = "Email",
 *     validators = listOf(
 *         Validators.notBlank,
 *         Validators.validEmail
 *     )
 * )
 * ```
 *
 * Tidak perlu pass `isError` atau `errorMessage` dari luar —
 * field menjalankan validator sendiri saat user mengetik.
 */
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    showInfoIcon: Boolean = false,
    suffixText: String? = null,
    validators: List<FieldValidator> = emptyList(),
) {
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
                // Jalankan semua validator, ambil error pertama yang gagal
                val failed = validators.map { it(newValue) }.firstOrNull { !it.isValid }
                isError = failed != null
                errorMessage = failed?.errorMessage
            },
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(label)
                    if (showInfoIcon) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            modifier = Modifier.width(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            placeholder = { Text(placeholder) },
            leadingIcon = leadingIcon,
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            trailingIcon = if (suffixText != null) {
                {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .height(56.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VerticalDivider(
                            modifier = Modifier.fillMaxHeight(),
                            color = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                            thickness = 1.dp
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(60.dp)
                                .background(
                                    color = Color(0xFFF5F5F9),
                                    shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = suffixText)
                        }
                    }
                }
            } else null
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}