package com.nexters.bandalart.android.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface RecentBandalartKeyRepository {
  suspend fun setRecentBandalartKey(recentBandalartKey: String)
  fun getRecentBandalartKey(): Flow<Result<String>>
}
