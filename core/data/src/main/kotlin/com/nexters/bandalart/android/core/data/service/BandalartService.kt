package com.nexters.bandalart.android.core.data.service

import com.nexters.bandalart.android.core.data.model.bandalart.BandalartCellResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartDetailResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartShareResponse
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartEmojiRequest
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartMainCellRequest
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartSubCellRequest
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartTaskCellRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface BandalartService {
  @POST("v1/bandalarts")
  suspend fun createBandalart(): Response<BandalartResponse>

  @GET("v1/bandalarts")
  suspend fun getBandalartList(): Response<List<BandalartDetailResponse>>

  @GET("v1/bandalarts/{bandalartKey}")
  suspend fun getBandalartDetail(
    @Path("bandalartKey") bandalartKey: String,
  ): Response<BandalartDetailResponse>

  @DELETE("v1/bandalarts/{bandalartKey}")
  suspend fun deleteBandalart(
    @Path("bandalartKey") bandalartKey: String,
  ): Response<Unit>

  @GET("v1/bandalarts/{bandalartKey}/cells")
  suspend fun getBandalartMainCell(
    @Path("bandalartKey") bandalartKey: String,
  ): Response<BandalartCellResponse>

  @GET("v1/bandalarts/{bandalartKey}/cells/{cellKey}")
  suspend fun getBandalartCell(
    @Path("bandalartKey") bandalartKey: String,
    @Path("cellKey") cellKey: String,
  ): Response<BandalartCellResponse>

  @PATCH("v1/bandalarts/{bandalartKey}/cells/{cellKey}")
  suspend fun updateBandalartMainCell(
    @Path("bandalartKey") bandalartKey: String,
    @Path("cellKey") cellKey: String,
    @Body updateBandalartMainCellRequest: UpdateBandalartMainCellRequest,
  ): Response<Unit>

  @PATCH("v1/bandalarts/{bandalartKey}/cells/{cellKey}")
  suspend fun updateBandalartSubCell(
    @Path("bandalartKey") bandalartKey: String,
    @Path("cellKey") cellKey: String,
    @Body updateBandalartSubCellRequest: UpdateBandalartSubCellRequest,
  ): Response<Unit>

  @PATCH("v1/bandalarts/{bandalartKey}/cells/{cellKey}")
  suspend fun updateBandalartTaskCell(
    @Path("bandalartKey") bandalartKey: String,
    @Path("cellKey") cellKey: String,
    @Body updateBandalartTaskCellRequest: UpdateBandalartTaskCellRequest,
  ): Response<Unit>

  @PATCH("v1/bandalarts/{bandalartKey}/cells/{cellKey}")
  suspend fun updateBandalartEmoji(
    @Path("bandalartKey") bandalartKey: String,
    @Path("cellKey") cellKey: String,
    @Body updateBandalartEmojiRequest: UpdateBandalartEmojiRequest,
  ): Response<Unit>

  @DELETE("v1/bandalarts/{bandalartKey}/cells/{cellKey}")
  suspend fun deleteBandalartCell(
    @Path("bandalartKey") bandalartKey: String,
    @Path("cellKey") cellKey: String,
  ): Response<Unit>

  @POST("v1/bandalarts/{bandalartKey}/shares")
  suspend fun shareBandalart(
    @Path("bandalartKey") bandalartKey: String,
  ): Response<BandalartShareResponse>
}
