package com.nexters.bandalart.android.core.data.model.bandalart

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BandalartResponse(
  @SerialName("key")
  val key: String,
  @SerialName("mainColor")
  val mainColor: String,
  @SerialName("subColor")
  val subColor: String,
  @SerialName("profileEmoji")
  val profileEmoji: String?,
  @SerialName("completionRatio")
  val completionRatio: Int,
)
