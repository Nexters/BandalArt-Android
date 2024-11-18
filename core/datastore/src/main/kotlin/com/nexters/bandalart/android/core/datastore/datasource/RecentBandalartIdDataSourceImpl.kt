package com.nexters.bandalart.android.core.datastore.datasource

import com.nexters.bandalart.android.core.datastore.DataStoreProvider
import javax.inject.Inject

internal class RecentBandalartIdDataSourceImpl @Inject constructor(
  private val datastoreProvider: DataStoreProvider,
) : RecentBandalartIdDataSource {

  override suspend fun setRecentBandalartId(recentBandalartId: Long) {
    datastoreProvider.setRecentBandalartId(recentBandalartId)
  }

  override suspend fun getRecentBandalartId(): Long {
    return datastoreProvider.getRecentBandalartId()
  }
}
