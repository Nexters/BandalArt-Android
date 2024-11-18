package com.nexters.bandalart.android.core.data.repository

import com.nexters.bandalart.android.core.data.mapper.toDto
import com.nexters.bandalart.android.core.data.mapper.toEntity
import com.nexters.bandalart.android.core.database.BandalartDao
import com.nexters.bandalart.android.core.datastore.datasource.CompletedBandalartIdDataSource
import com.nexters.bandalart.android.core.datastore.datasource.RecentBandalartIdDataSource
import com.nexters.bandalart.android.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartMainCellEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartSubCellEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartTaskCellEntity
import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import javax.inject.Inject

internal class BandalartRepositoryImpl @Inject constructor(
  // private val bandalartRemoteDataSource: BandalartRemoteDataSource,
  private val recentBandalartIdDataSource: RecentBandalartIdDataSource,
  private val completedBandalartIdDataSource: CompletedBandalartIdDataSource,
  private val bandalartDao: BandalartDao,
) : BandalartRepository {
  override suspend fun createBandalart(): BandalartEntity {
    // return bandalartRemoteDataSource.createBandalart()?.toEntity()
    val bandalartId = bandalartDao.createEmptyBandalart()
    return bandalartDao.getBandalart(bandalartId).toEntity()
  }

  override suspend fun getBandalartList(): List<BandalartDetailEntity> {
    // return bandalartRemoteDataSource.getBandalartList()?.map { it.toEntity() }
    return bandalartDao.getBandalartList().map { it.toEntity() }
  }

  override suspend fun getBandalartDetail(bandalartId: Long): BandalartDetailEntity? {
    // return bandalartRemoteDataSource.getBandalartDetail(bandalartKey)?.toEntity()
    return bandalartDao.getBandalartDetail(bandalartId).toEntity()
  }

  override suspend fun deleteBandalart(bandalartId: Long) {
    // bandalartRemoteDataSource.deleteBandalart(bandalartKey)
    bandalartDao.getBandalart(bandalartId).let {
      bandalartDao.deleteBandalart(it)
    }
  }

  override suspend fun getBandalartMainCell(bandalartId: Long): BandalartCellEntity? {
    // return bandalartRemoteDataSource.getBandalartMainCell(bandalartKey)?.toEntity()
    return bandalartDao.getBandalartMainCell(bandalartId).cell.toEntity()
  }

  override suspend fun getBandalartCell(bandalartId: Long, cellId: Long): BandalartCellEntity? {
    // return bandalartRemoteDataSource.getBandalartCell(bandalartKey, cellKey)?.toEntity()
    return bandalartDao.getBandalartCell(cellId).cell.toEntity()
  }

  override suspend fun updateBandalartMainCell(
    bandalartId: Long,
    cellId: Long,
    updateBandalartMainCellEntity: UpdateBandalartMainCellEntity,
  ) {
    // bandalartRemoteDataSource.updateBandalartMainCell(bandalartKey, cellKey, updateBandalartMainCellEntity.toModel())
    bandalartDao.updateMainCellWithDto(
      cellId,
      updateBandalartMainCellEntity.toDto()
    )
  }

  override suspend fun updateBandalartSubCell(
    bandalartId: Long,
    cellId: Long,
    updateBandalartSubCellEntity: UpdateBandalartSubCellEntity,
  ) {
    // bandalartRemoteDataSource.updateBandalartSubCell(bandalartKey, cellKey, updateBandalartSubCellEntity.toModel())
    bandalartDao.updateSubCellWithDto(
      cellId,
      updateBandalartSubCellEntity.toDto()
    )
  }

  override suspend fun updateBandalartTaskCell(
    bandalartId: Long,
    cellId: Long,
    updateBandalartTaskCellEntity: UpdateBandalartTaskCellEntity,
  ) {
    // bandalartRemoteDataSource.updateBandalartTaskCell(bandalartKey, cellKey, updateBandalartTaskCellEntity.toModel())
    bandalartDao.updateTaskCellWithDto(
      cellId,
      updateBandalartTaskCellEntity.toDto()
    )
  }

  override suspend fun updateBandalartEmoji(
    bandalartId: Long,
    cellId: Long,
    updateBandalartEmojiEntity: UpdateBandalartEmojiEntity,
  ) {
    // bandalartRemoteDataSource.updateBandalartEmoji(bandalartKey, cellKey, updateBandalartEmojiEntity.toModel())
    bandalartDao.updateEmojiWithDto(
      cellId,
      updateBandalartEmojiEntity.toDto()
    )
  }

  override suspend fun deleteBandalartCell(bandalartId: Long, cellId: Long) {
    // bandalartRemoteDataSource.deleteBandalartCell(bandalartKey, cellKey)
    bandalartDao.deleteBandalartCell(cellId)
  }

  override suspend fun setRecentBandalartId(recentBandalartId: Long) {
    recentBandalartIdDataSource.setRecentBandalartId(recentBandalartId)
  }

  override suspend fun getRecentBandalartId(): Long {
    return recentBandalartIdDataSource.getRecentBandalartId()
  }

//  override suspend fun shareBandalart(bandalartKey: String): BandalartShareEntity? {
//    return bandalartRemoteDataSource.shareBandalart(bandalartKey)?.toEntity()
//  }

  override suspend fun getPrevBandalartList(): List<Pair<Long, Boolean>> {
    return completedBandalartIdDataSource.getPrevBandalartList()
  }

  override suspend fun upsertBandalartId(bandalartId: Long, isCompleted: Boolean) {
    completedBandalartIdDataSource.upsertBandalartId(bandalartId, isCompleted)
  }

  override suspend fun checkCompletedBandalartId(bandalartId: Long): Boolean {
    return completedBandalartIdDataSource.checkCompletedBandalartId(bandalartId)
  }

  override suspend fun deleteBandalartId(bandalartId: Long) {
    completedBandalartIdDataSource.deleteBandalartId(bandalartId)
  }
}
