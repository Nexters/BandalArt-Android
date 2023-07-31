package com.nexters.bandalart.android.core.data.datasource

import com.nexters.bandalart.android.core.data.model.bandalart.BandalartDetailResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartListResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartCellResponse
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartCellRequest

interface BandalartRemoteDataSource {
  suspend fun createBandalart()
  suspend fun getBandalartList(): BandalartListResponse?

  suspend fun getBandalartDetail(bandalartKey: String): BandalartDetailResponse?

  suspend fun getBandalartMainCell(bandalartKey: String): BandalartCellResponse?

  suspend fun getBandalartCell(bandalartKey: String, cellKey: String): BandalartCellResponse?

  suspend fun updateBandalartCell(bandalartKey: String, cellKey: String, updateBandalartRequest: UpdateBandalartCellRequest)

  suspend fun deleteBandalartCell(bandalartKey: String, cellKey: String)
}
