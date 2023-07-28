package com.nexters.bandalart.android.core.data.remote.datasource

import com.nexters.bandalart.android.core.data.datasource.BandalartRemoteDataSource
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartDetailResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartListResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartCellResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class BandalartRemoteDataSourceImpl @Inject constructor(
  private val client: HttpClient,
) : BandalartRemoteDataSource {
  override suspend fun getBandalartList(): BandalartListResponse? {
    return client
      .get("v1/bandalarts")
      .body()
  }

  override suspend fun getBandalartDetail(bandalartKey: String): BandalartDetailResponse? {
    return client
      .get("v1/bandalarts/$bandalartKey")
      .body()
  }

  override suspend fun getBandalartMainCell(bandalartKey: String): BandalartCellResponse? {
    return client
      .get("v1/bandalarts/$bandalartKey/cells")
      .body()
  }
}
