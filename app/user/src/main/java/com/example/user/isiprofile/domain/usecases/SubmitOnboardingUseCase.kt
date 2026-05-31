package com.example.user.isiprofile.domain.usecases

import com.example.core.model.BaseState
import com.example.user.isiprofile.domain.entities.OnboardingPayload
import com.example.user.isiprofile.domain.repository.OnboardingRepository

class SubmitOnboardingUseCase(private val repository: OnboardingRepository) {
    suspend operator fun invoke(payload: OnboardingPayload): BaseState =
        repository.submitOnboardingData(payload)
}
