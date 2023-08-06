package com.nexters.bandalart.android.core.data.datasource

import com.nexters.bandalart.android.core.data.model.bandalart.BandalartDetailResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartCellResponse
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartMainCellRequest
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartSubCellRequest
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartTaskCellRequest

interface BandalartRemoteDataSource {
  suspend fun createBandalart()

  suspend fun getBandalartList(): List<BandalartDetailResponse>?

  suspend fun getBandalartDetail(bandalartKey: String): BandalartDetailResponse?

  suspend fun deleteBandalart(bandalartKey: String)

  suspend fun getBandalartMainCell(bandalartKey: String): BandalartCellResponse?

  suspend fun getBandalartCell(bandalartKey: String, cellKey: String): BandalartCellResponse?

  suspend fun updateBandalartMainCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartMainCellRequest: UpdateBandalartMainCellRequest,
  )

  suspend fun updateBandalartSubCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartSubCellRequest: UpdateBandalartSubCellRequest,
  )

  suspend fun updateBandalartTaskCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartTaskCellRequest: UpdateBandalartTaskCellRequest,
  )

  suspend fun deleteBandalartCell(bandalartKey: String, cellKey: String)
}
