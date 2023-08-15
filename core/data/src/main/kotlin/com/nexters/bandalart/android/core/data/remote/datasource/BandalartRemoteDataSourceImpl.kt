@file:OptIn(InternalAPI::class)

package com.nexters.bandalart.android.core.data.remote.datasource

import com.nexters.bandalart.android.core.data.datasource.BandalartRemoteDataSource
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartCellResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartDetailResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartShareResponse
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartEmojiRequest
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartMainCellRequest
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartSubCellRequest
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartTaskCellRequest
import com.nexters.bandalart.android.core.data.util.extension.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.util.InternalAPI
import javax.inject.Inject

internal class BandalartRemoteDataSourceImpl @Inject constructor(
  private val client: HttpClient,
) : BandalartRemoteDataSource {
  override suspend fun createBandalart(): BandalartResponse? {
    return client.safeRequest {
      post("v1/bandalarts").body()
    }
  }

  override suspend fun getBandalartList(): List<BandalartDetailResponse>? {
    return client.safeRequest {
      get("v1/bandalarts").body()
    }
  }

  override suspend fun getBandalartDetail(bandalartKey: String): BandalartDetailResponse? {
    return client.safeRequest {
      get("v1/bandalarts/$bandalartKey").body()
    }
  }

  override suspend fun deleteBandalart(bandalartKey: String) {
    client.safeRequest {
      delete("v1/bandalarts/$bandalartKey")
    }
  }

  override suspend fun getBandalartMainCell(bandalartKey: String): BandalartCellResponse? {
    return client.safeRequest {
      get("v1/bandalarts/$bandalartKey/cells").body()
    }
  }

  override suspend fun getBandalartCell(bandalartKey: String, cellKey: String): BandalartCellResponse? {
    return client.safeRequest {
      get("v1/bandalarts/$bandalartKey/cells/$cellKey").body()
    }
  }

  override suspend fun updateBandalartMainCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartMainCellRequest: UpdateBandalartMainCellRequest,
  ) {
    client.safeRequest {
      patch("v1/bandalarts/$bandalartKey/cells/$cellKey") {
        setBody(updateBandalartMainCellRequest)
      }
    }
  }

  override suspend fun updateBandalartSubCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartSubCellRequest: UpdateBandalartSubCellRequest,
  ) {
    client.safeRequest {
      patch("v1/bandalarts/$bandalartKey/cells/$cellKey") {
        setBody(updateBandalartSubCellRequest)
      }
    }
  }

  override suspend fun updateBandalartTaskCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartTaskCellRequest: UpdateBandalartTaskCellRequest,
  ) {
    client.safeRequest {
      patch("v1/bandalarts/$bandalartKey/cells/$cellKey") {
        setBody(updateBandalartTaskCellRequest)
      }
    }
  }

  override suspend fun updateBandalartEmoji(
    bandalartKey: String,
    cellKey: String,
    updateBandalartEmojiRequest: UpdateBandalartEmojiRequest,
  ) {
    client.safeRequest {
      patch("v1/bandalarts/$bandalartKey/cells/$cellKey") {
        setBody(updateBandalartEmojiRequest)
      }
    }
  }

  override suspend fun deleteBandalartCell(bandalartKey: String, cellKey: String) {
    client.safeRequest {
      delete("v1/bandalarts/$bandalartKey/cells/$cellKey")
    }
  }

  override suspend fun shareBandalart(bandalartKey: String): BandalartShareResponse? {
    return client.safeRequest {
      post("v1/bandalarts/$bandalartKey/shares").body()
    }
  }
}
