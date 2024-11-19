package com.nexters.bandalart.core.datastore.datasource

import com.nexters.bandalart.core.datastore.DataStoreProvider
import javax.inject.Inject

internal class OnboardingDataSourceImpl @Inject constructor(
    private val datastoreProvider: DataStoreProvider,
) : OnboardingDataSource {
    override suspend fun setOnboardingCompletedStatus(flag: Boolean) {
        datastoreProvider.setOnboardingCompletedStatus(flag)
    }

    override suspend fun getOnboardingCompletedStatus(): Boolean {
        return datastoreProvider.getOnboardingCompletedStatus()
    }
}
