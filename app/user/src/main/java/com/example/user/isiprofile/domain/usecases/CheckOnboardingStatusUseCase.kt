package com.example.user.isiprofile.domain.usecases

import com.example.user.isiprofile.domain.repository.OnboardingRepository

class CheckOnboardingStatusUseCase(private val repository: OnboardingRepository) {
    suspend operator fun invoke(): Boolean = repository.checkIsOnboardingComplete()
}
