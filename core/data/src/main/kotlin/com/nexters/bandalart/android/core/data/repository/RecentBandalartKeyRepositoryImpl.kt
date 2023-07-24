package com.nexters.bandalart.android.core.data.repository

import com.nexters.bandalart.android.core.data.datasource.RecentBandalartKeyDataSource
import com.nexters.bandalart.android.core.domain.repository.RecentBandalartKeyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class RecentBandalartKeyRepositoryImpl @Inject constructor(private val dataSource: RecentBandalartKeyDataSource) :
  RecentBandalartKeyRepository {
  override suspend fun setRecentBandalartKey(recentBandalartKey: String) {
    dataSource.setRecentBandalartKey(recentBandalartKey)
  }

  override fun getRecentBandalartKey(): Flow<Result<String>> {
    return dataSource.getRecentBandalartKey()
  }
}
