package com.nexters.bandalart.android.core.domain.usecase.bandalart

import com.nexters.bandalart.android.core.domain.repository.OnboardingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetOnboardingCompletedStatusUseCase @Inject constructor(
  private val repository: OnboardingRepository,
) {
  suspend operator fun invoke(flag: Boolean) {
    repository.setOnboardingCompletedStatus(flag)
  }
}
