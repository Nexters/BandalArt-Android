package com.nexters.bandalart.android.core.datastore.datasource

interface RecentBandalartIdDataSource {
  suspend fun setRecentBandalartId(recentBandalartId: Long)
  suspend fun getRecentBandalartId(): Long
}
