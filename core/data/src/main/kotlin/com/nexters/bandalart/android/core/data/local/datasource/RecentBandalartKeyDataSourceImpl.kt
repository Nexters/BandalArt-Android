package com.nexters.bandalart.android.core.data.local.datasource

import com.nexters.bandalart.android.core.data.datasource.RecentBandalartKeyDataSource
import com.nexters.bandalart.android.core.data.local.DataStoreProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class RecentBandalartKeyDataSourceImpl @Inject constructor(private val datastoreProvider: DataStoreProvider) :
  RecentBandalartKeyDataSource {

  override suspend fun setRecentBandalartKey(recentBandalartKey: String) {
    datastoreProvider.setRecentBandalartKey(recentBandalartKey)
  }

  override fun getRecentBandalartKey(): Flow<Result<String>> {
    return datastoreProvider.getRecentBandalartKey()
  }
}
