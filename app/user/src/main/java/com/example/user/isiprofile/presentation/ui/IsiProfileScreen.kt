package com.example.user.isiprofile.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.component.Validators
import com.example.core.component.field.CustomTextField
import com.example.core.component.field.DropDownField
import com.example.core.model.BaseState
import com.example.user.isiprofile.presentation.state.IsiProfileState
import com.example.user.isiprofile.presentation.viewmodel.IsiProfileViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IsiProfileScreen(
    onProfileComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IsiProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.submitState) {
        if (state.submitState is BaseState.Success) {
            onProfileComplete()
        }
    }

    Scaffold(
        topBar = {
            if (pagerState.currentPage == 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = "Isi Informasi Kendaraan",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Isi Profil Kamu",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Pager Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(Color(0xFFFF6D00), RoundedCornerShape(2.dp))
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(
                            if (pagerState.currentPage == 1) Color(0xFFFF6D00) else Color(0xFFE0E0E0),
                            RoundedCornerShape(2.dp)
                        )
                )
            }

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> Page1(
                        state = state,
                        viewModel = viewModel,
                        onNext = {
                            if (state.isPage1Valid) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(1)
                                }
                            }
                        }
                    )
                    1 -> Page2(
                        state = state,
                        viewModel = viewModel,
                        onSubmit = {
                            viewModel.submitForm()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Page1(
    state: IsiProfileState,
    viewModel: IsiProfileViewModel,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Tambahkan profile singkat untuk memudahkan proses service.",
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(32.dp))

        CustomTextField(
            value = state.nama,
            onValueChange = viewModel::onNamaChange,
            label = "Nama",
            placeholder = "contoh: redi setiadi",
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
            },
            validators = listOf(Validators.notBlank)
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(
            value = state.phone,
            onValueChange = viewModel::onPhoneChange,
            label = "Nomor Handphone",
            placeholder = "contoh: 081234567890",
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Default.Phone, contentDescription = null, tint = Color.Gray)
            },
            numberOnly = true,
            validators = listOf(Validators.notBlank, Validators.minLength(10))
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6D00)),
            enabled = state.isPage1Valid
        ) {
            Text("Selanjutnya", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun Page2(
    state: IsiProfileState,
    viewModel: IsiProfileViewModel,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical)
            .padding(24.dp)
    ) {
        Text(
            text = "Tambahkan informasi kendaraan untuk perawatan yang relevan.",
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Pilih Jenis Kendaraan", fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            VehicleTypeCard(
                modifier = Modifier.size(60.dp),
                imageRes = com.example.user.R.drawable.motor,
                contentDescription = "Motor",
                isSelected = state.jenisKendaraan == "motor",
                onClick = { viewModel.onJenisKendaraanChange("motor") }
            )
            VehicleTypeCard(
                modifier = Modifier.size(60.dp),
                imageRes = com.example.user.R.drawable.mobil,
                contentDescription = "Mobil",
                isSelected = state.jenisKendaraan == "mobil",
                onClick = { viewModel.onJenisKendaraanChange("mobil") }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        val merkOptions = listOf("Honda", "Yamaha", "Suzuki", "Kawasaki", "Toyota", "Daihatsu")
        DropDownField(
            items = merkOptions,
            selectedItem = state.merkKendaraan,
            onItemSelected = viewModel::onMerkKendaraanChange,
            label = "Merk Kendaraan",
            placeholder = "Pilih merk kendaraan",
            isRequired = state.submitAttempted,
            enableSearch = true,
            searchPlaceholder = "Cari merk"
        )
        Spacer(modifier = Modifier.height(16.dp))

        val tipeOptions = listOf("Matic", "Manual", "Bebek", "Sport", "SUV", "Sedan")
        DropDownField(
            items = tipeOptions,
            selectedItem = state.tipeKendaraan,
            onItemSelected = viewModel::onTipeKendaraanChange,
            label = "Tipe Kendaraan",
            placeholder = "Pilih tipe kendaraan",
            isRequired = state.submitAttempted,
            enableSearch = true,
            searchPlaceholder = "Cari tipe"
        )
        Spacer(modifier = Modifier.height(16.dp))

        val tahunOptions = (2024 downTo 2000).map { it.toString() }
        DropDownField(
            items = tahunOptions,
            selectedItem = state.tahunKendaraan,
            onItemSelected = viewModel::onTahunKendaraanChange,
            label = "Tahun Kendaraan",
            placeholder = "Pilih tahun kendaraan",
            isRequired = state.submitAttempted,
            enableSearch = true,
            searchPlaceholder = "Cari tahun"
        )
        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = state.totalKm,
            onValueChange = viewModel::onTotalKmChange,
            label = "Total Kilometer",
            placeholder = "Masukan total kilometer",
            modifier = Modifier.fillMaxWidth(),
            suffixText = "km",
            showInfoIcon = true,
            numberOnly = true,
            validators = listOf(Validators.notBlank)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6D00)),
            enabled = state.submitState !is BaseState.Loading && state.isPage2Valid
        ) {
            if (state.submitState is BaseState.Loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Mulai Pantau", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

/**
 * Card pemilih jenis kendaraan.
 * - Saat dipilih: border oranye 2dp, background oranye pudar, badge check di sudut kanan atas.
 * - Saat tidak dipilih: border abu-abu, background putih/surface.
 */
@Composable
private fun VehicleTypeCard(
    imageRes: Int,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color(0xFFFF6D00) else Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = if (isSelected) Color(0xFFFFF3E0) else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = androidx.compose.ui.res.painterResource(id = imageRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(25.dp)
        )

        // Badge check oranye di sudut kanan atas
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(22.dp)
                    .background(Color(0xFFFF6D00), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}