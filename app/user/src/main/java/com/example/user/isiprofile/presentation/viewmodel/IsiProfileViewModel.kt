package com.example.user.isiprofile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.component.Validators
import com.example.core.model.BaseState
import com.example.user.isiprofile.domain.entities.OnboardingPayload
import com.example.user.isiprofile.domain.usecases.SubmitOnboardingUseCase
import com.example.user.isiprofile.presentation.state.IsiProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IsiProfileViewModel(
    private val submitOnboardingUseCase: SubmitOnboardingUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(IsiProfileState())
    val state: StateFlow<IsiProfileState> = _state.asStateFlow()

    fun onNamaChange(nama: String) {
        _state.update { it.copy(nama = nama, isPage1Valid = validatePage1(nama, it.phone)) }
    }

    fun onPhoneChange(phone: String) {
        _state.update { it.copy(phone = phone, isPage1Valid = validatePage1(it.nama, phone)) }
    }

    fun onJenisKendaraanChange(jenis: String) {
        _state.update { it.copy(jenisKendaraan = jenis) }
    }

    fun onMerkKendaraanChange(merk: String) {
        _state.update {
            it.copy(
                merkKendaraan = merk,
                isPage2Valid = validatePage2(merk, it.tipeKendaraan, it.tahunKendaraan, it.totalKm)
            )
        }
    }

    fun onTipeKendaraanChange(tipe: String) {
        _state.update {
            it.copy(
                tipeKendaraan = tipe,
                isPage2Valid = validatePage2(it.merkKendaraan, tipe, it.tahunKendaraan, it.totalKm)
            )
        }
    }

    fun onTahunKendaraanChange(tahun: String) {
        _state.update {
            it.copy(
                tahunKendaraan = tahun,
                isPage2Valid = validatePage2(it.merkKendaraan, it.tipeKendaraan, tahun, it.totalKm)
            )
        }
    }

    fun onTotalKmChange(km: String) {
        if (km.isEmpty() || km.all { it.isDigit() }) {
            _state.update {
                it.copy(
                    totalKm = km,
                    isPage2Valid = validatePage2(
                        it.merkKendaraan,
                        it.tipeKendaraan,
                        it.tahunKendaraan,
                        km
                    )
                )
            }
        }
    }

    /** Dipanggil ketika user menekan "Mulai Pantau" — baru show validation di dropdown */
    fun onSubmitAttempted() {
        _state.update { it.copy(submitAttempted = true) }
    }

    private fun validatePage1(nama: String, phone: String): Boolean {
        return Validators.notBlank(nama).isValid &&
                Validators.notBlank(phone).isValid &&
                Validators.minLength(10)(phone).isValid
    }

    private fun validatePage2(merk: String, tipe: String, tahun: String, km: String): Boolean {
        return merk.isNotBlank() && tipe.isNotBlank() && tahun.isNotBlank() && km.isNotBlank()
    }

    fun submitForm() {
        val currentState = _state.value

        // Mark as attempted so dropdown errors show
        _state.update { it.copy(submitAttempted = true) }

        if (!currentState.isPage2Valid || !currentState.isPage1Valid) return

        viewModelScope.launch {
            _state.update { it.copy(submitState = BaseState.Loading) }
            val payload = OnboardingPayload(
                username = currentState.nama,
                phone = currentState.phone,
                jenisKendaraan = currentState.jenisKendaraan,
                merkKendaraan = currentState.merkKendaraan,
                tipeKendaraan = currentState.tipeKendaraan,
                tahunKendaraan = currentState.tahunKendaraan.toIntOrNull() ?: 2000,
                totalKm = currentState.totalKm.toIntOrNull() ?: 0
            )
            val result = submitOnboardingUseCase(payload)
            _state.update { it.copy(submitState = result) }
        }
    }
}

