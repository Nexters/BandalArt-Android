package com.nexters.bandalart.core.data.repository

import com.nexters.bandalart.core.datastore.InAppUpdateDataStore
import com.nexters.bandalart.core.domain.repository.InAppUpdateRepository
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class InAppUpdateRepositoryImpl @Inject constructor(
    private val inAppUpdateDataStore: InAppUpdateDataStore,
) : InAppUpdateRepository {
    override suspend fun setLastRejectedUpdateVersion(rejectedVersionCode: Int) {
        inAppUpdateDataStore.setLastRejectedUpdateVersion(rejectedVersionCode)
    }

    override suspend fun isUpdateAlreadyRejected(updateVersionCode: Int): Boolean {
        val lastRejectedUpdateVersion = inAppUpdateDataStore.getLastRejectedUpdateVersion()
        return updateVersionCode <= lastRejectedUpdateVersion
    }
}
