package com.example.core.component.field

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownField(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "Pilih ${label.lowercase()}",
    isRequired: Boolean = false,
    enableSearch: Boolean = false,
    searchPlaceholder: String = "Cari ${label.lowercase()}",
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Tampilkan search query di field saat expanded, selecedItem saat tidak
    val displayValue = if (expanded && enableSearch) searchQuery else selectedItem

    val filteredItems = remember(items, searchQuery) {
        if (searchQuery.isNotBlank()) items.filter { it.contains(searchQuery, ignoreCase = true) }
        else items
    }

    val showError = isError || (isRequired && selectedItem.isBlank())
    val borderColor = if (showError) MaterialTheme.colorScheme.error
                      else MaterialTheme.colorScheme.outline

    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (showError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = it
                if (!it) searchQuery = ""
            }
        ) {
            // Field utama menjadi search input saat expanded —
            // readOnly = false + PrimaryEditable agar keyboard muncul otomatis
            OutlinedTextField(
                value = displayValue,
                onValueChange = { if (enableSearch && expanded) searchQuery = it },
                readOnly = !(enableSearch && expanded),
                placeholder = {
                    Text(
                        if (expanded && enableSearch) searchPlaceholder else placeholder,
                        color = Color.Gray
                    )
                },
                // Icon search muncul di leading saat mode search aktif
                leadingIcon = if (expanded && enableSearch) {
                    { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) }
                } else null,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (expanded) MaterialTheme.colorScheme.primary else borderColor,
                    unfocusedBorderColor = borderColor,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                ),
                isError = showError,
                singleLine = true,
                modifier = Modifier
                    // PrimaryEditable agar keyboard muncul saat field difokus
                    .menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                    searchQuery = ""
                },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                if (filteredItems.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Tidak ada hasil") },
                        onClick = {}
                    )
                } else {
                    filteredItems.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onItemSelected(option)
                                expanded = false
                                searchQuery = ""
                            }
                        )
                    }
                }
            }
        }

        val errorText = errorMessage
            ?: if (showError && selectedItem.isBlank()) "Bidang ini wajib diisi" else null
        if (showError && errorText != null) {
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}