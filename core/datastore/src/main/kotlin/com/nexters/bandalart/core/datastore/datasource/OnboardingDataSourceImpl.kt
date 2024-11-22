package com.nexters.bandalart.core.datastore.datasource

import com.nexters.bandalart.core.datastore.BandalartDataStore
import javax.inject.Inject

internal class OnboardingDataSourceImpl @Inject constructor(
    private val datastoreBandalart: BandalartDataStore,
) : OnboardingDataSource {
    override suspend fun setOnboardingCompletedStatus(flag: Boolean) {
        datastoreBandalart.setOnboardingCompletedStatus(flag)
    }

    override suspend fun getOnboardingCompletedStatus(): Boolean {
        return datastoreBandalart.getOnboardingCompletedStatus()
    }
}
