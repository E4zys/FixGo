package com.example.user.isiprofile.domain.repository

import com.example.core.model.BaseState
import com.example.user.isiprofile.domain.entities.OnboardingPayload

interface OnboardingRepository {
    suspend fun checkIsOnboardingComplete(): Boolean
    suspend fun submitOnboardingData(payload: OnboardingPayload): BaseState
}
