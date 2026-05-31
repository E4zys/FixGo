package com.example.user.isiprofile.data.repository

import com.example.core.model.BaseState
import com.example.user.isiprofile.data.datasource.OnboardingDataSource
import com.example.user.isiprofile.domain.entities.OnboardingPayload
import com.example.user.isiprofile.domain.repository.OnboardingRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth

class OnboardingRepositoryImpl(
    private val dataSource: OnboardingDataSource,
    private val supabaseClient: SupabaseClient
) : OnboardingRepository {

    override suspend fun checkIsOnboardingComplete(): Boolean = try {
        val userId = supabaseClient.auth.currentUserOrNull()?.id ?: return false
        dataSource.checkProfileComplete(userId) && dataSource.checkHasKendaraan(userId)
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    override suspend fun submitOnboardingData(payload: OnboardingPayload): BaseState = try {
        val userId = supabaseClient.auth.currentUserOrNull()?.id
            ?: return BaseState.Error("User tidak ditemukan")
        dataSource.updateUserProfile(userId, payload.username, payload.phone)
        dataSource.insertKendaraan(payload, userId)
        BaseState.Success
    } catch (e: Exception) {
        e.printStackTrace()
        BaseState.Error(e.message ?: "Terjadi kesalahan saat menyimpan data")
    }
}
