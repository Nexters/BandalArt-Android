package com.nexters.bandalart.core.datastore.datasource

interface OnboardingDataSource {
    suspend fun setOnboardingCompletedStatus(flag: Boolean)
    suspend fun getOnboardingCompletedStatus(): Boolean
}
