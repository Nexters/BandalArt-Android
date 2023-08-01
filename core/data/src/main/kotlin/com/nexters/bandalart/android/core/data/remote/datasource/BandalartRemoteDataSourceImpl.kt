@file:OptIn(InternalAPI::class)

package com.nexters.bandalart.android.core.data.remote.datasource

import com.nexters.bandalart.android.core.data.datasource.BandalartRemoteDataSource
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartCellResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartDetailResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartListResponse
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartCellRequest
import com.nexters.bandalart.android.core.data.util.ApiException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
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
    try {
      client
        .post("v1/bandalarts")
    } catch (e: ResponseException) {
      throw ApiException(e.response.status.value, e.response.status.description)
    }
  }

  override suspend fun getBandalartList(): BandalartListResponse? {
    return try {
      client
        .get("v1/bandalarts")
        .body()
    } catch  (e: ResponseException) {
      throw ApiException(e.response.status.value, e.response.status.description)
    }
  }

  override suspend fun getBandalartDetail(bandalartKey: String): BandalartDetailResponse? {
    return try {
      client
        .get("v1/bandalarts/$bandalartKey")
        .body()
    } catch (e: ResponseException) {
      throw ApiException(e.response.status.value, e.response.status.description)
    }
  }

  override suspend fun deleteBandalart(bandalartKey: String) {
    try {
      client
        .delete("v1/bandalarts/$bandalartKey")
    } catch(e: ResponseException) {
      throw ApiException(e.response.status.value, e.response.status.description)
    }
  }

  override suspend fun getBandalartMainCell(bandalartKey: String): BandalartCellResponse? {
    return try {
      client
        .get("v1/bandalarts/$bandalartKey/cells")
        .body()
    } catch(e: ResponseException) {
      throw ApiException(e.response.status.value, e.response.status.description)
    }
  }

  override suspend fun getBandalartCell(bandalartKey: String, cellKey: String): BandalartCellResponse? {
    return try {
      client
        .get("v1/bandalarts/$bandalartKey/cells/$cellKey")
        .body()
    } catch(e: ResponseException) {
      throw ApiException(e.response.status.value, e.response.status.description)
    }
  }

  override suspend fun updateBandalartCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartRequest: UpdateBandalartCellRequest,
  ) {
    try {
      client
        .patch("v1/bandalarts/$bandalartKey/cells/$cellKey") {
          setBody(updateBandalartRequest)
        }
    } catch(e: ResponseException) {
      throw ApiException(e.response.status.value, e.response.status.description)
    }
  }

  override suspend fun deleteBandalartCell(bandalartKey: String, cellKey: String) {
    try {
      client
        .delete("v1/bandalarts/$bandalartKey/cells/$cellKey")
    } catch (e: ResponseException) {
      throw ApiException(e.response.status.value, e.response.status.description)
    }
  }
}
