package com.nexters.bandalart.android.core.data.datasource

import com.nexters.bandalart.android.core.network.model.bandalart.BandalartCellResponse
import com.nexters.bandalart.android.core.network.model.bandalart.BandalartDetailResponse
import com.nexters.bandalart.android.core.network.model.bandalart.BandalartResponse
import com.nexters.bandalart.android.core.network.model.bandalart.BandalartShareResponse
import com.nexters.bandalart.android.core.network.model.bandalart.UpdateBandalartEmojiRequest
import com.nexters.bandalart.android.core.network.model.bandalart.UpdateBandalartMainCellRequest
import com.nexters.bandalart.android.core.network.model.bandalart.UpdateBandalartSubCellRequest
import com.nexters.bandalart.android.core.network.model.bandalart.UpdateBandalartTaskCellRequest
import com.nexters.bandalart.android.core.network.service.BandalartService
import javax.inject.Inject

internal class BandalartRemoteDataSourceImpl @Inject constructor(
    // private val client: HttpClient,
    private val service: BandalartService,
) : BandalartRemoteDataSource {
    override suspend fun createBandalart(): BandalartResponse {
//    return client.safeRequest {
//      post("v1/bandalarts").body()
//    }
        return service.createBandalart()
    }

    override suspend fun getBandalartList(): List<BandalartDetailResponse> {
//    return client.safeRequest {
//      get("v1/bandalarts").body()
//    }
        return service.getBandalartList()
    }

    override suspend fun getBandalartDetail(bandalartKey: String): BandalartDetailResponse {
//    return client.safeRequest {
//      get("v1/bandalarts/$bandalartKey").body()
//    }
        return service.getBandalartDetail(bandalartKey)
    }

    override suspend fun deleteBandalart(bandalartKey: String) {
//    client.safeRequest {
//      delete("v1/bandalarts/$bandalartKey")
//    }
        service.deleteBandalart(bandalartKey)
    }

    override suspend fun getBandalartMainCell(bandalartKey: String): BandalartCellResponse {
//    return client.safeRequest {
//      get("v1/bandalarts/$bandalartKey/cells").body()
//    }
        return service.getBandalartMainCell(bandalartKey)
    }

    override suspend fun getBandalartCell(bandalartKey: String, cellKey: String): BandalartCellResponse {
//    return client.safeRequest {
//      get("v1/bandalarts/$bandalartKey/cells/$cellKey").body()
//    }
        return service.getBandalartCell(bandalartKey, cellKey)
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
        service.updateBandalartMainCell(bandalartKey, cellKey, updateBandalartMainCellRequest)
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
        service.updateBandalartSubCell(bandalartKey, cellKey, updateBandalartSubCellRequest)
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
        service.updateBandalartTaskCell(bandalartKey, cellKey, updateBandalartTaskCellRequest)
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
        service.updateBandalartEmoji(bandalartKey, cellKey, updateBandalartEmojiRequest)
    }

    override suspend fun deleteBandalartCell(bandalartKey: String, cellKey: String) {
//    client.safeRequest {
//      delete("v1/bandalarts/$bandalartKey/cells/$cellKey")
//    }
        service.deleteBandalartCell(bandalartKey, cellKey)
    }

    override suspend fun shareBandalart(bandalartKey: String): BandalartShareResponse {
//    return client.safeRequest {
//      post("v1/bandalarts/$bandalartKey/shares").body()
//    }
        return service.shareBandalart(bandalartKey)
    }
}
