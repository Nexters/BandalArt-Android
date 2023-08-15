package com.nexters.bandalart.android.core.data.repository

import com.nexters.bandalart.android.core.data.datasource.BandalartRemoteDataSource
import com.nexters.bandalart.android.core.data.datasource.CompletedBandalartKeyDataSource
import com.nexters.bandalart.android.core.data.datasource.RecentBandalartKeyDataSource
import com.nexters.bandalart.android.core.data.mapper.toEntity
import com.nexters.bandalart.android.core.data.mapper.toModel
import com.nexters.bandalart.android.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartShareEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartMainCellEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartSubCellEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartTaskCellEntity
import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import javax.inject.Inject

internal class BandalartRepositoryImpl @Inject constructor(
  private val bandalartRemoteDataSource: BandalartRemoteDataSource,
  private val recentBandalartKeyDataSource: RecentBandalartKeyDataSource,
  private val completedBandalartKeyDataSource: CompletedBandalartKeyDataSource,
) : BandalartRepository {
  override suspend fun createBandalart(): BandalartEntity? {
    return bandalartRemoteDataSource.createBandalart()?.toEntity()
  }

  override suspend fun getBandalartList(): List<BandalartDetailEntity>? {
    return bandalartRemoteDataSource.getBandalartList()?.map { it.toEntity() }
  }

  override suspend fun getBandalartDetail(bandalartKey: String): BandalartDetailEntity? {
    return bandalartRemoteDataSource.getBandalartDetail(bandalartKey)?.toEntity()
  }

  override suspend fun deleteBandalart(bandalartKey: String) {
    bandalartRemoteDataSource.deleteBandalart(bandalartKey)
  }

  override suspend fun getBandalartMainCell(bandalartKey: String): BandalartCellEntity? {
    return bandalartRemoteDataSource.getBandalartMainCell(bandalartKey)?.toEntity()
  }

  override suspend fun getBandalartCell(bandalartKey: String, cellKey: String): BandalartCellEntity? {
    return bandalartRemoteDataSource.getBandalartCell(bandalartKey, cellKey)?.toEntity()
  }

  override suspend fun updateBandalartMainCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartMainCellEntity: UpdateBandalartMainCellEntity,
  ) {
    bandalartRemoteDataSource.updateBandalartMainCell(bandalartKey, cellKey, updateBandalartMainCellEntity.toModel())
  }

  override suspend fun updateBandalartSubCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartSubCellEntity: UpdateBandalartSubCellEntity,
  ) {
    bandalartRemoteDataSource.updateBandalartSubCell(bandalartKey, cellKey, updateBandalartSubCellEntity.toModel())
  }

  override suspend fun updateBandalartTaskCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartTaskCellEntity: UpdateBandalartTaskCellEntity,
  ) {
    bandalartRemoteDataSource.updateBandalartTaskCell(bandalartKey, cellKey, updateBandalartTaskCellEntity.toModel())
  }
  override suspend fun deleteBandalartCell(bandalartKey: String, cellKey: String) {
    bandalartRemoteDataSource.deleteBandalartCell(bandalartKey, cellKey)
  }

  override suspend fun setRecentBandalartKey(recentBandalartKey: String) {
    recentBandalartKeyDataSource.setRecentBandalartKey(recentBandalartKey)
  }

  override suspend fun getRecentBandalartKey(): String {
    return recentBandalartKeyDataSource.getRecentBandalartKey()
  }

  override suspend fun shareBandalart(bandalartKey: String): BandalartShareEntity? {
    return bandalartRemoteDataSource.shareBandalart(bandalartKey)?.toEntity()
  }

  override suspend fun getPrevBandalartList(): List<Pair<String, Boolean>> {
    return completedBandalartKeyDataSource.getPrevBandalartList()
  }

  override suspend fun upsertBandalartKey(bandalartKey: String, isCompleted: Boolean) {
    completedBandalartKeyDataSource.upsertBandalartKey(bandalartKey, isCompleted)
  }

  override suspend fun checkCompletedBandalartKey(bandalartKey: String): Boolean {
    return completedBandalartKeyDataSource.checkCompletedBandalartKey(bandalartKey)
  }

  override suspend fun deleteBandalartKey(bandalartKey: String) {
    completedBandalartKeyDataSource.deleteBandalartKey(bandalartKey)
  }
}
