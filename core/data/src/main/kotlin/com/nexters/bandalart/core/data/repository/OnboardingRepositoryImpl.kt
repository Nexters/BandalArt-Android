package com.nexters.bandalart.core.data.repository

import com.nexters.bandalart.core.datastore.BandalartDataStore
import com.nexters.bandalart.core.domain.repository.OnboardingRepository
import javax.inject.Inject

internal class OnboardingRepositoryImpl @Inject constructor(
    private val bandalartDataStore: BandalartDataStore,
) : OnboardingRepository {
    override suspend fun setOnboardingCompletedStatus(flag: Boolean) {
        bandalartDataStore.setOnboardingCompletedStatus(flag)
    }

    override suspend fun getOnboardingCompletedStatus(): Boolean {
        return bandalartDataStore.getOnboardingCompletedStatus()
    }
}
