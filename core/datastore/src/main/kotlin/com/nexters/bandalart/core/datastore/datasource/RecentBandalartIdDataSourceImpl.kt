package com.nexters.bandalart.core.datastore.datasource

import com.nexters.bandalart.core.datastore.BandalartDataStore
import javax.inject.Inject

internal class RecentBandalartIdDataSourceImpl @Inject constructor(
    private val datastoreBandalart: BandalartDataStore,
) : RecentBandalartIdDataSource {

    override suspend fun setRecentBandalartId(recentBandalartId: Long) {
        datastoreBandalart.setRecentBandalartId(recentBandalartId)
    }

    override suspend fun getRecentBandalartId(): Long {
        return datastoreBandalart.getRecentBandalartId()
    }
}
