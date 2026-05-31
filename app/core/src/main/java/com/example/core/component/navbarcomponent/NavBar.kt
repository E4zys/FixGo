package com.example.core.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.R





@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    isHomeSelected: Boolean = false,
    isAktivitasSelected: Boolean = false,
    isRiwayatSelected: Boolean = false,
    isAkunSelected: Boolean = false,
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onNotifClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ItemNavBar(
                    itemText = "Beranda",
                    idPainter = R.drawable.home,
                    onClick = onHomeClick,
                    isSelected = isHomeSelected
                )
                ItemNavBar(
                    itemText = "Aktivitas",
                    idPainter = R.drawable.clipboard_list,
                    onClick = onSearchClick,
                    isSelected = isAktivitasSelected
                )

                Spacer(modifier = Modifier.width(68.dp))

                ItemNavBar(
                    itemText = "Riwayat",
                    idPainter = R.drawable.clock,
                    onClick = onNotifClick,
                    isSelected = isRiwayatSelected
                )
                ItemNavBar(
                    itemText = "Akun",
                    idPainter = R.drawable.user,
                    onClick = onProfileClick,
                    isSelected = isAkunSelected
                )
            }
            Box(modifier = Modifier.height(34.dp).fillMaxWidth())
        }
    }
}

@Preview
@Composable
private fun NavBarPrev() {

}