@file:OptIn(InternalAPI::class)

package com.nexters.bandalart.android.core.data.remote.datasource

import com.nexters.bandalart.android.core.data.datasource.BandalartRemoteDataSource
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartDetailResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartListResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartCellResponse
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartCellRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.util.InternalAPI
import javax.inject.Inject

class BandalartRemoteDataSourceImpl @Inject constructor(
  private val client: HttpClient,
) : BandalartRemoteDataSource {
  override suspend fun createBandalart() {
    client
      .post("v1/bandalarts")
  }

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

  override suspend fun getBandalartCell(bandalartKey: String, cellKey: String): BandalartCellResponse? {
    return client
      .get("v1/bandalarts/$bandalartKey/cells/$cellKey")
      .body()
  }

  override suspend fun updateBandalartCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartRequest: UpdateBandalartCellRequest,
  ) {
    client
      .patch("v1/bandalarts/$bandalartKey/cells/$cellKey") {
        setBody(updateBandalartRequest)
      }
  }

  override suspend fun deleteBandalartCell(bandalartKey: String, cellKey: String) {
    client
      .delete("v1/bandalarts/$bandalartKey/cells/$cellKey")
  }
}
