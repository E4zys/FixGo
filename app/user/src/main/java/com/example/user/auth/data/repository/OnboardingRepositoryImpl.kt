package com.example.user.auth.data.repository

import com.example.core.model.BaseState
import com.example.user.auth.data.datasource.OnboardingDataSource
import com.example.user.auth.domain.entities.OnboardingPayload
import com.example.user.auth.domain.repository.OnboardingRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth

class OnboardingRepositoryImpl(
    private val dataSource: OnboardingDataSource,
    private val supabaseClient: SupabaseClient
) : OnboardingRepository {

    override suspend fun checkIsOnboardingComplete(): Boolean {
        return try {
            val user = supabaseClient.auth.currentUserOrNull() ?: return false
            val userId = user.id
            val profileComplete = dataSource.checkProfileComplete(userId)
            val hasKendaraan = dataSource.checkHasKendaraan(userId)
            profileComplete && hasKendaraan
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun submitOnboardingData(payload: OnboardingPayload): BaseState {
        return try {
            val user = supabaseClient.auth.currentUserOrNull()
                ?: return BaseState.Error("User tidak ditemukan")
            val userId = user.id

            dataSource.updateUserProfile(userId, payload.username, payload.phone)
            dataSource.insertKendaraan(payload, userId)

            BaseState.Success
        } catch (e: Exception) {
            e.printStackTrace()
            BaseState.Error(e.message ?: "Terjadi kesalahan saat menyimpan data")
        }
    }
}

