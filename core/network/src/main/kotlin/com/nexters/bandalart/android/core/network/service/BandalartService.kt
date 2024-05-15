package com.nexters.bandalart.android.core.network.service

import com.nexters.bandalart.android.core.network.model.bandalart.BandalartCellResponse
import com.nexters.bandalart.android.core.network.model.bandalart.BandalartDetailResponse
import com.nexters.bandalart.android.core.network.model.bandalart.BandalartResponse
import com.nexters.bandalart.android.core.network.model.bandalart.BandalartShareResponse
import com.nexters.bandalart.android.core.network.model.bandalart.UpdateBandalartEmojiRequest
import com.nexters.bandalart.android.core.network.model.bandalart.UpdateBandalartMainCellRequest
import com.nexters.bandalart.android.core.network.model.bandalart.UpdateBandalartSubCellRequest
import com.nexters.bandalart.android.core.network.model.bandalart.UpdateBandalartTaskCellRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface BandalartService {
  @POST("v1/bandalarts")
  suspend fun createBandalart(): BandalartResponse

  @GET("v1/bandalarts")
  suspend fun getBandalartList(): List<BandalartDetailResponse>

  @GET("v1/bandalarts/{bandalartKey}")
  suspend fun getBandalartDetail(
    @Path("bandalartKey") bandalartKey: String,
  ): BandalartDetailResponse

  @DELETE("v1/bandalarts/{bandalartKey}")
  suspend fun deleteBandalart(
    @Path("bandalartKey") bandalartKey: String,
  ): Unit

  @GET("v1/bandalarts/{bandalartKey}/cells")
  suspend fun getBandalartMainCell(
    @Path("bandalartKey") bandalartKey: String,
  ): BandalartCellResponse

  @GET("v1/bandalarts/{bandalartKey}/cells/{cellKey}")
  suspend fun getBandalartCell(
    @Path("bandalartKey") bandalartKey: String,
    @Path("cellKey") cellKey: String,
  ): BandalartCellResponse

  @PATCH("v1/bandalarts/{bandalartKey}/cells/{cellKey}")
  suspend fun updateBandalartMainCell(
    @Path("bandalartKey") bandalartKey: String,
    @Path("cellKey") cellKey: String,
    @Body updateBandalartMainCellRequest: UpdateBandalartMainCellRequest,
  )

  @PATCH("v1/bandalarts/{bandalartKey}/cells/{cellKey}")
  suspend fun updateBandalartSubCell(
    @Path("bandalartKey") bandalartKey: String,
    @Path("cellKey") cellKey: String,
    @Body updateBandalartSubCellRequest: UpdateBandalartSubCellRequest,
  )

  @PATCH("v1/bandalarts/{bandalartKey}/cells/{cellKey}")
  suspend fun updateBandalartTaskCell(
    @Path("bandalartKey") bandalartKey: String,
    @Path("cellKey") cellKey: String,
    @Body updateBandalartTaskCellRequest: UpdateBandalartTaskCellRequest,
  )

  @PATCH("v1/bandalarts/{bandalartKey}/cells/{cellKey}")
  suspend fun updateBandalartEmoji(
    @Path("bandalartKey") bandalartKey: String,
    @Path("cellKey") cellKey: String,
    @Body updateBandalartEmojiRequest: UpdateBandalartEmojiRequest,
  )

  @DELETE("v1/bandalarts/{bandalartKey}/cells/{cellKey}")
  suspend fun deleteBandalartCell(
    @Path("bandalartKey") bandalartKey: String,
    @Path("cellKey") cellKey: String,
  )

  @POST("v1/bandalarts/{bandalartKey}/shares")
  suspend fun shareBandalart(
    @Path("bandalartKey") bandalartKey: String,
  ): BandalartShareResponse
}
