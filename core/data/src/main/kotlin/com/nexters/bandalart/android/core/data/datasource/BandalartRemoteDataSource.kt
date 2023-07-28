package com.nexters.bandalart.android.core.data.datasource

import com.nexters.bandalart.android.core.data.model.bandalart.BandalartDetailResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartListResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartMainCellResponse

interface BandalartRemoteDataSource {
  suspend fun getBandalartList(): BandalartListResponse?

  suspend fun getBandalartDetail(bandalartKey: String): BandalartDetailResponse?

  suspend fun getBandalartMainCell(bandalartKey: String): BandalartMainCellResponse?
}
