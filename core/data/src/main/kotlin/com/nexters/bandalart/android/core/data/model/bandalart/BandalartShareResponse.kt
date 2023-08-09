package com.nexters.bandalart.android.core.data.model.bandalart

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BandalartShareResponse(
  @SerialName("shareUrl")
  val shareUrl: String,
  @SerialName("key")
  val key: String,
  @SerialName("endDate")
  val endDate: String,
)
