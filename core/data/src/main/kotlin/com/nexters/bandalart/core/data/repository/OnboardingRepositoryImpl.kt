package com.nexters.bandalart.core.data.repository

import com.nexters.bandalart.core.datastore.BandalartDataStore
import com.nexters.bandalart.core.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class OnboardingRepositoryImpl @Inject constructor(
    private val bandalartDataStore: BandalartDataStore,
) : OnboardingRepository {
    override suspend fun setOnboardingCompletedStatus(flag: Boolean) {
        bandalartDataStore.setOnboardingCompletedStatus(flag)
    }

    override fun flowIsOnboardingCompleted(): Flow<Boolean> {
        return bandalartDataStore.flowIsOnboardingCompleted()
    }
}
