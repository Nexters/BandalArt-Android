package com.nexters.bandalart.android.core.data.datasource

import kotlinx.coroutines.flow.Flow

interface RecentBandalartKeyDataSource {
  suspend fun setRecentBandalartKey(recentBandalartKey: String)
  fun getRecentBandalartKey(): Flow<Result<String>>
}
