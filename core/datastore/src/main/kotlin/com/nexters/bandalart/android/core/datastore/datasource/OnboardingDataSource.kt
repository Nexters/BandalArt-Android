package com.nexters.bandalart.android.core.datastore.datasource

interface OnboardingDataSource {
  suspend fun setOnboardingCompletedStatus(flag: Boolean)
  suspend fun getOnboardingCompletedStatus(): Boolean
}
