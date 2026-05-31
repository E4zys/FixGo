package com.example.user.auth.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.core.component.snackbar.ObserveSnackbar
import com.example.user.R
import com.example.core.R as CoreR

/**
 * Layout dasar untuk halaman autentikasi (Login & Daftar).
 *
 * Menyediakan:
 * - Background header biru
 * - Logo + judul + subtitle
 * - Card putih yang kontennya bisa dikustomisasi via [formContent]
 * - Social login icons
 * - Row teks navigasi antar halaman via [bottomLinkContent]
 *
 * @param title          Judul utama di header (mis. "Masuk ke FixGo")
 * @param subtitle       Teks deskripsi di bawah judul
 * @param formContent    Konten form di dalam card (field, button, dll.)
 * @param bottomLinkContent  Row teks di bawah (mis. "Belum punya akun? Daftar sekarang")
 */
@Composable
fun BaseAuthLayout(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    formContent: @Composable ColumnScope.() -> Unit,
    bottomLinkContent: @Composable () -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            // ── Header background biru ──────────────────────────────────────
            Box(
                modifier = modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                    )
                    .height(333.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ── Logo + Judul + Subtitle ─────────────────────────────────
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        space = 16.dp,
                        alignment = Alignment.CenterVertically
                    )
                ) {
                    Surface(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        shadowElevation = 4.dp,
                        shape = RoundedCornerShape(28.dp),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 8.dp)
                                .height(60.dp)
                                .width(41.dp),
                            painter = painterResource(R.drawable.image),
                            contentDescription = null,
                        )
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }

                // ── Form Card ───────────────────────────────────────────────
                Surface(
                    shadowElevation = 4.dp,
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(space = 20.dp),
                        content = formContent
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ── Social Login ────────────────────────────────────────────
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f))
                        Text(
                            text = "Atau lanjutkan dengan",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                    ) {
                        listOf(CoreR.drawable.fb, CoreR.drawable.apple, CoreR.drawable.google)
                            .forEach { icon ->
                                Surface(
                                    shape = CircleShape,
                                    shadowElevation = 2.dp,
                                    modifier = Modifier.size(50.dp),
                                    onClick = {}
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Image(
                                            painter = painterResource(id = icon),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                    }
                }

                // ── Bottom link (Daftar / Masuk) ────────────────────────────
                bottomLinkContent()

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
