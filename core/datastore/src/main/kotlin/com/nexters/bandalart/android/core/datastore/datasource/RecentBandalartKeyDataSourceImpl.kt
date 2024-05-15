package com.nexters.bandalart.android.core.datastore.datasource

import com.nexters.bandalart.android.core.datastore.DataStoreProvider
import javax.inject.Inject

internal class RecentBandalartKeyDataSourceImpl @Inject constructor(
  private val datastoreProvider: DataStoreProvider,
) : RecentBandalartKeyDataSource {

  override suspend fun setRecentBandalartKey(recentBandalartKey: String) {
    datastoreProvider.setRecentBandalartKey(recentBandalartKey)
  }

  override suspend fun getRecentBandalartKey(): String {
    return datastoreProvider.getRecentBandalartKey()
  }
}
