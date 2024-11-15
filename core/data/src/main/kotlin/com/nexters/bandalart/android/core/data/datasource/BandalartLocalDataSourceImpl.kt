package com.nexters.bandalart.android.core.data.datasource

import com.nexters.bandalart.android.core.data.mapper.toDBEntity
import com.nexters.bandalart.android.core.data.mapper.toEntity
import com.nexters.bandalart.android.core.database.BandalartDao
import com.nexters.bandalart.android.core.database.entity.BandalartCellWithChildrenDto
import com.nexters.bandalart.android.core.database.entity.UpdateBandalartEmojiDto
import com.nexters.bandalart.android.core.database.entity.UpdateBandalartMainCellDto
import com.nexters.bandalart.android.core.database.entity.UpdateBandalartSubCellDto
import com.nexters.bandalart.android.core.database.entity.UpdateBandalartTaskCellDto
import com.nexters.bandalart.android.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartEntity
import javax.inject.Inject

class BandalartLocalDataSourceImpl @Inject constructor(
  private val bandalartDao: BandalartDao,
): BandalartLocalDataSource {
  override suspend fun createBandalart(bandalart: BandalartEntity): Long {
    return bandalartDao.createBandalart(bandalart.toDBEntity())
  }

  override suspend fun getBandalartList(): List<BandalartDetailEntity> {
    return bandalartDao.getBandalartList().map { it.toEntity() }
  }

  override suspend fun getBandalartDetail(bandalartId: String): BandalartDetailEntity {
    return bandalartDao.getBandalartDetail(bandalartId).toEntity()
  }

  override suspend fun deleteBandalart(bandalart: BandalartEntity) {
    bandalartDao.deleteBandalart(bandalart.toDBEntity())
  }

  override suspend fun getBandalartMainCell(bandalartId: String): BandalartCellWithChildrenDto {
    return bandalartDao.getBandalartMainCell(bandalartId)
  }

  override suspend fun getBandalartCell(bandalartId: String, cellId: String): BandalartCellWithChildrenDto {
    return bandalartDao.getBandalartCell(cellId)
  }

  override suspend fun updateBandalartMainCell(bandalartId: String, cellId: String, updateDto: UpdateBandalartMainCellDto) {
    bandalartDao.updateMainCellWithDto(cellId, updateDto)
  }

  override suspend fun updateBandalartSubCell(bandalartId: String, cellId: String, updateDto: UpdateBandalartSubCellDto) {
    bandalartDao.updateSubCellWithDto(cellId, updateDto)
  }

  override suspend fun updateBandalartTaskCell(bandalartId: String, cellId: String, updateDto: UpdateBandalartTaskCellDto) {
    bandalartDao.updateTaskCellWithDto(cellId, updateDto)
  }

  override suspend fun updateBandalartEmoji(bandalartId: String, cellId: String, updateDto: UpdateBandalartEmojiDto) {
    bandalartDao.updateEmojiWithDto(cellId, updateDto)
  }

  override suspend fun deleteBandalartCell(bandalartId: String, cellId: String) {
    bandalartDao.deleteBandalartCell(cellId)
  }
}
