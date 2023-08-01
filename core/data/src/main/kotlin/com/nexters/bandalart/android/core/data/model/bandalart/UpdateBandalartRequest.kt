package com.nexters.bandalart.android.core.data.model.bandalart

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateBandalartCellRequest(
  @SerialName("title")
  val title: String,
  @SerialName("description")
  val description: String?,
  @SerialName("dueDate")
  val dueDate: String?,
  @SerialName("isCompleted")
  val isCompleted: Boolean,
)
