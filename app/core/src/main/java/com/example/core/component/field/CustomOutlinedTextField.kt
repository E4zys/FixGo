package com.example.core.component.field

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
 * Ketika [suffixText] diisi, border menyatu mengelilingi field + suffix (km, dll).
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
    numberOnly: Boolean = false,
    validators: List<FieldValidator> = emptyList(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val validate: (String) -> Unit = { newValue ->
        val failed = validators.map { it(newValue) }.firstOrNull { !it.isValid }
        isError = failed != null
        errorMessage = failed?.errorMessage
    }

    // Kalau numberOnly, paksa KeyboardType.Number dan filter karakter non-digit
    val resolvedKeyboardOptions = if (numberOnly) {
        keyboardOptions.copy(keyboardType = KeyboardType.Number)
    } else {
        keyboardOptions
    }

    val filteredOnValueChange: (String) -> Unit = { newValue ->
        val processed = if (numberOnly) newValue.filter { it.isDigit() } else newValue
        onValueChange(processed)
        validate(processed)
    }

    Column(modifier = modifier) {
        if (suffixText != null) {
            // ── Suffix mode: outer Row dengan border menyatu ──────────────────
            val interactionSource = remember { MutableInteractionSource() }
            val isFocused by interactionSource.collectIsFocusedAsState()

            val borderColor = when {
                isError -> MaterialTheme.colorScheme.error
                isFocused -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.outline
            }

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(label)
                    if (showInfoIcon) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            modifier = Modifier.width(16.dp).padding(bottom = 4.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(1.dp, borderColor, RoundedCornerShape(8.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // OutlinedTextField tanpa border (border dari outer Row)
                    OutlinedTextField(
                        value = value,
                        onValueChange = filteredOnValueChange,
                        placeholder = { Text(placeholder) },
                        leadingIcon = leadingIcon,
                        isError = isError,
                        singleLine = true,
                        interactionSource = interactionSource,
                        keyboardOptions = resolvedKeyboardOptions,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            errorBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            errorContainerColor = Color.Transparent,
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    // Divider pemisah
                    VerticalDivider(
                        modifier = Modifier.height(56.dp),
                        color = borderColor,
                        thickness = 1.dp
                    )

                    // Suffix box (km, dll)
                    Box(
                        modifier = Modifier
                            .height(56.dp)
                            .width(60.dp)
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = suffixText, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        } else {
            // ── Normal mode: OutlinedTextField biasa ──────────────────────────
            OutlinedTextField(
                value = value,
                onValueChange = filteredOnValueChange,
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
                keyboardOptions = resolvedKeyboardOptions,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                )
            )
        }

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