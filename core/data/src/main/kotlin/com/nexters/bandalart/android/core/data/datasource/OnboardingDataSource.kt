package com.nexters.bandalart.android.core.data.datasource

interface OnboardingDataSource {
  suspend fun setOnboardingCompletedStatus(flag: Boolean)
  suspend fun getOnboardingCompletedStatus(): Boolean
}
