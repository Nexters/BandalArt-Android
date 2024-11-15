package com.nexters.bandalart.android.core.data.datasource

import com.nexters.bandalart.android.core.database.entity.BandalartCellWithChildrenDto
import com.nexters.bandalart.android.core.database.entity.UpdateBandalartEmojiDto
import com.nexters.bandalart.android.core.database.entity.UpdateBandalartMainCellDto
import com.nexters.bandalart.android.core.database.entity.UpdateBandalartSubCellDto
import com.nexters.bandalart.android.core.database.entity.UpdateBandalartTaskCellDto
import com.nexters.bandalart.android.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartEntity

interface BandalartLocalDataSource {
  suspend fun createBandalart(bandalart: BandalartEntity): Long

  suspend fun getBandalartList(): List<BandalartDetailEntity>

  suspend fun getBandalartDetail(bandalartId: String): BandalartDetailEntity

  suspend fun deleteBandalart(bandalart: BandalartEntity)

  suspend fun getBandalartMainCell(bandalartId: String): BandalartCellWithChildrenDto

  suspend fun getBandalartCell(bandalartId: String, cellId: String): BandalartCellWithChildrenDto

  suspend fun updateBandalartMainCell(
    bandalartId: String,
    cellId: String,
    updateDto: UpdateBandalartMainCellDto,
  )

  suspend fun updateBandalartSubCell(
    bandalartId: String,
    cellId: String,
    updateDto: UpdateBandalartSubCellDto,
  )

  suspend fun updateBandalartTaskCell(
    bandalartId: String,
    cellId: String,
    updateDto: UpdateBandalartTaskCellDto,
  )

  suspend fun updateBandalartEmoji(
    bandalartId: String,
    cellId: String,
    updateDto: UpdateBandalartEmojiDto,
  )

  suspend fun deleteBandalartCell(bandalartId: String, cellId: String)
}
