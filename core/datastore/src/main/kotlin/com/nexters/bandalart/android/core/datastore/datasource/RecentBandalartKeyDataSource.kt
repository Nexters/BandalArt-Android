package com.nexters.bandalart.android.core.datastore.datasource

interface RecentBandalartKeyDataSource {
  suspend fun setRecentBandalartKey(recentBandalartKey: String)
  suspend fun getRecentBandalartKey(): String
}
