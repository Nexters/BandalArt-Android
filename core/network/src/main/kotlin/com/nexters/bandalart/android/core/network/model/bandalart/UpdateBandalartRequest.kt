package com.nexters.bandalart.android.core.network.model.bandalart

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateBandalartMainCellRequest(
  @SerialName("title")
  val title: String?,
  @SerialName("description")
  val description: String?,
  @SerialName("dueDate")
  val dueDate: String?,
  @SerialName("profileEmoji")
  val profileEmoji: String?,
  @SerialName("mainColor")
  val mainColor: String,
  @SerialName("subColor")
  val subColor: String,
)

@Serializable
data class UpdateBandalartSubCellRequest(
  @SerialName("title")
  val title: String?,
  @SerialName("description")
  val description: String?,
  @SerialName("dueDate")
  val dueDate: String?,
)

@Serializable
data class UpdateBandalartTaskCellRequest(
  @SerialName("title")
  val title: String?,
  @SerialName("description")
  val description: String?,
  @SerialName("dueDate")
  val dueDate: String?,
  @SerialName("isCompleted")
  val isCompleted: Boolean? = null,
)

@Serializable
data class UpdateBandalartEmojiRequest(
  @SerialName("profileEmoji")
  val profileEmoji: String?,
)
