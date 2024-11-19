package com.nexters.bandalart.core.datastore.datasource

import com.nexters.bandalart.core.datastore.DataStoreProvider
import javax.inject.Inject

internal class CompletedBandalartIdDataStoreImpl @Inject constructor(
    private val datastoreProvider: DataStoreProvider,
) : CompletedBandalartIdDataSource {
    override suspend fun getPrevBandalartList(): List<Pair<Long, Boolean>> {
        return datastoreProvider.getPrevBandalartList()
    }

    override suspend fun upsertBandalartId(bandalartId: Long, isCompleted: Boolean) {
        datastoreProvider.upsertBandalartId(bandalartId, isCompleted)
    }

    override suspend fun checkCompletedBandalartId(bandalartId: Long): Boolean {
        return datastoreProvider.checkCompletedBandalartId(bandalartId)
    }

    override suspend fun deleteBandalartId(bandalartId: Long) {
        datastoreProvider.deleteBandalartId(bandalartId)
    }
}
