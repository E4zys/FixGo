package com.example.user.isiprofile.data.datasource

import com.example.user.isiprofile.domain.entities.OnboardingPayload

interface OnboardingDataSource {
    suspend fun checkProfileComplete(userId: String): Boolean
    suspend fun checkHasKendaraan(userId: String): Boolean
    suspend fun updateUserProfile(userId: String, username: String, phone: String)
    suspend fun insertKendaraan(payload: OnboardingPayload, userId: String)
}
