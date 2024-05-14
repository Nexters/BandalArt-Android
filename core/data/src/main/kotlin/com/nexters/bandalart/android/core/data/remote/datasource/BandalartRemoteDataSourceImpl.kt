package com.nexters.bandalart.android.core.data.remote.datasource

import com.nexters.bandalart.android.core.data.datasource.BandalartRemoteDataSource
import com.nexters.bandalart.android.core.network.model.bandalart.BandalartCellResponse
import com.nexters.bandalart.android.core.network.model.bandalart.BandalartDetailResponse
import com.nexters.bandalart.android.core.network.model.bandalart.BandalartResponse
import com.nexters.bandalart.android.core.network.model.bandalart.BandalartShareResponse
import com.nexters.bandalart.android.core.network.model.bandalart.UpdateBandalartEmojiRequest
import com.nexters.bandalart.android.core.network.model.bandalart.UpdateBandalartMainCellRequest
import com.nexters.bandalart.android.core.network.model.bandalart.UpdateBandalartSubCellRequest
import com.nexters.bandalart.android.core.network.model.bandalart.UpdateBandalartTaskCellRequest
import com.nexters.bandalart.android.core.network.service.BandalartService
import com.nexters.bandalart.android.core.data.util.extension.safeRequest
import javax.inject.Inject

internal class BandalartRemoteDataSourceImpl @Inject constructor(
  // private val client: HttpClient,
  private val service: BandalartService,
) : BandalartRemoteDataSource {
  override suspend fun createBandalart(): BandalartResponse? {
//    return client.safeRequest {
//      post("v1/bandalarts").body()
//    }
    return safeRequest {
      service.createBandalart()
    }
  }

  override suspend fun getBandalartList(): List<BandalartDetailResponse>? {
//    return client.safeRequest {
//      get("v1/bandalarts").body()
//    }
    return safeRequest {
      service.getBandalartList()
    }
  }

  override suspend fun getBandalartDetail(bandalartKey: String): BandalartDetailResponse? {
//    return client.safeRequest {
//      get("v1/bandalarts/$bandalartKey").body()
//    }
    return safeRequest {
      service.getBandalartDetail(bandalartKey)
    }
  }

  override suspend fun deleteBandalart(bandalartKey: String) {
//    client.safeRequest {
//      delete("v1/bandalarts/$bandalartKey")
//    }
    safeRequest {
      service.deleteBandalart(bandalartKey)
    }
  }

  override suspend fun getBandalartMainCell(bandalartKey: String): BandalartCellResponse? {
//    return client.safeRequest {
//      get("v1/bandalarts/$bandalartKey/cells").body()
//    }
    return safeRequest {
      service.getBandalartMainCell(bandalartKey)
    }
  }

  override suspend fun getBandalartCell(bandalartKey: String, cellKey: String): BandalartCellResponse? {
//    return client.safeRequest {
//      get("v1/bandalarts/$bandalartKey/cells/$cellKey").body()
//    }
    return safeRequest {
      service.getBandalartCell(bandalartKey, cellKey)
    }
  }

  override suspend fun updateBandalartMainCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartMainCellRequest: UpdateBandalartMainCellRequest,
  ) {
//    client.safeRequest {
//      patch("v1/bandalarts/$bandalartKey/cells/$cellKey") {
//        setBody(updateBandalartMainCellRequest)
//      }
//    }
    safeRequest {
      service.updateBandalartMainCell(bandalartKey, cellKey, updateBandalartMainCellRequest)
    }
  }

  override suspend fun updateBandalartSubCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartSubCellRequest: UpdateBandalartSubCellRequest,
  ) {
//    client.safeRequest {
//      patch("v1/bandalarts/$bandalartKey/cells/$cellKey") {
//        setBody(updateBandalartSubCellRequest)
//      }
//    }
    safeRequest {
      service.updateBandalartSubCell(bandalartKey, cellKey, updateBandalartSubCellRequest)
    }
  }

  override suspend fun updateBandalartTaskCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartTaskCellRequest: UpdateBandalartTaskCellRequest,
  ) {
//    client.safeRequest {
//      patch("v1/bandalarts/$bandalartKey/cells/$cellKey") {
//        setBody(updateBandalartTaskCellRequest)
//      }
//    }
    safeRequest {
      service.updateBandalartTaskCell(bandalartKey, cellKey, updateBandalartTaskCellRequest)
    }
  }

  override suspend fun updateBandalartEmoji(
    bandalartKey: String,
    cellKey: String,
    updateBandalartEmojiRequest: UpdateBandalartEmojiRequest,
  ) {
//    client.safeRequest {
//      patch("v1/bandalarts/$bandalartKey/cells/$cellKey") {
//        setBody(updateBandalartEmojiRequest)
//      }
//    }
    safeRequest {
      service.updateBandalartEmoji(bandalartKey, cellKey, updateBandalartEmojiRequest)
    }
  }

  override suspend fun deleteBandalartCell(bandalartKey: String, cellKey: String) {
//    client.safeRequest {
//      delete("v1/bandalarts/$bandalartKey/cells/$cellKey")
//    }
    safeRequest {
      service.deleteBandalartCell(bandalartKey, cellKey)
    }
  }

  override suspend fun shareBandalart(bandalartKey: String): BandalartShareResponse? {
//    return client.safeRequest {
//      post("v1/bandalarts/$bandalartKey/shares").body()
//    }
    return safeRequest {
      service.shareBandalart(bandalartKey)
    }
  }
}
