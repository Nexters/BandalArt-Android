package com.nexters.bandalart.android.core.data.datasource

interface RecentBandalartKeyDataSource {
  suspend fun setRecentBandalartKey(recentBandalartKey: String)
  suspend fun getRecentBandalartKey(): String
}
