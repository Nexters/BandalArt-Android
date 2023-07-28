package com.nexters.bandalart.android.core.data.repository

import com.nexters.bandalart.android.core.data.datasource.BandalartRemoteDataSource
import com.nexters.bandalart.android.core.data.datasource.RecentBandalartKeyDataSource
import com.nexters.bandalart.android.core.data.mapper.toEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartListEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartMainCellEntity
import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import javax.inject.Inject

class BandalartRepositoryImpl @Inject constructor(
  private val bandalartRemoteDataSource: BandalartRemoteDataSource,
  private val recentBandalartKeyDataSource: RecentBandalartKeyDataSource,
) : BandalartRepository {
  override suspend fun getBandalartList(): BandalartListEntity? {
    return bandalartRemoteDataSource.getBandalartList()?.toEntity()
  }

  override suspend fun getBandalartDetail(bandalartKey: String): BandalartDetailEntity? {
    return bandalartRemoteDataSource.getBandalartDetail(bandalartKey)?.toEntity()
  }

  override suspend fun getBandalartMainCell(bandalartKey: String): BandalartMainCellEntity? {
    return bandalartRemoteDataSource.getBandalartMainCell(bandalartKey)?.toEntity()
  }

  override suspend fun setRecentBandalartKey(recentBandalartKey: String) {
    recentBandalartKeyDataSource.setRecentBandalartKey(recentBandalartKey)
  }

  override suspend fun getRecentBandalartKey(): String {
    return recentBandalartKeyDataSource.getRecentBandalartKey()
  }
}
