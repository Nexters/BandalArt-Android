package com.nexters.bandalart.android.core.data.repository

import com.nexters.bandalart.android.core.datastore.datasource.OnboardingDataSource
import com.nexters.bandalart.android.core.domain.repository.OnboardingRepository
import javax.inject.Inject

internal class OnboardingRepositoryImpl @Inject constructor(
    private val dataSource: OnboardingDataSource,
) : OnboardingRepository {
    override suspend fun setOnboardingCompletedStatus(flag: Boolean) {
        dataSource.setOnboardingCompletedStatus(flag)
    }

    override suspend fun getOnboardingCompletedStatus(): Boolean {
        return dataSource.getOnboardingCompletedStatus()
    }
}
