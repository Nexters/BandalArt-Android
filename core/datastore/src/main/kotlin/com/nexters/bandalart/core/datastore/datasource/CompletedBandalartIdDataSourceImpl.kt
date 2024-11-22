package com.nexters.bandalart.core.datastore.datasource

import com.nexters.bandalart.core.datastore.BandalartDataStore
import javax.inject.Inject

internal class CompletedBandalartIdDataSourceImpl @Inject constructor(
    private val bandalartDataStore: BandalartDataStore,
) : CompletedBandalartIdDataSource {
    override suspend fun getPrevBandalartList(): List<Pair<Long, Boolean>> {
        return bandalartDataStore.getPrevBandalartList()
    }

    override suspend fun upsertBandalartId(bandalartId: Long, isCompleted: Boolean) {
        bandalartDataStore.upsertBandalartId(bandalartId, isCompleted)
    }

    override suspend fun checkCompletedBandalartId(bandalartId: Long): Boolean {
        return bandalartDataStore.checkCompletedBandalartId(bandalartId)
    }

    override suspend fun deleteBandalartId(bandalartId: Long) {
        bandalartDataStore.deleteBandalartId(bandalartId)
    }
}
