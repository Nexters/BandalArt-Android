package com.nexters.bandalart.android.core.data.model.bandalart

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BandalartDetailResponse(
  @SerialName("key")
  val key: String,
  @SerialName("cellKey")
  val cellKey: String,
  @SerialName("dueDate")
  val dueDate: String,
  @SerialName("isCompleted")
  val isCompleted: Boolean,
  @SerialName("sharekey")
  val shareKey: String,
)
