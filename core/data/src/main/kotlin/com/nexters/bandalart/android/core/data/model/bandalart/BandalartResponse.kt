package com.nexters.bandalart.android.core.data.model.bandalart

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BandalartResponse(
  @SerialName("id")
  val id: String,
  @SerialName("key")
  val key: String,
  @SerialName("mainColor")
  val mainColor: String,
  @SerialName("subColor")
  val subColor: String,
  @SerialName("profileEmoji")
  val profileEmoji: String?,
  @SerialName("cellId")
  val cellId: Int,
  @SerialName("shareId")
  val shareId: String?,
  @SerialName("userId")
  val userId: Int,
  @SerialName("createdAt")
  val createdAt: String,
  @SerialName("updatedAt")
  val updatedAt: String,
  @SerialName("deletedAt")
  val deletedAt: String?,
)
