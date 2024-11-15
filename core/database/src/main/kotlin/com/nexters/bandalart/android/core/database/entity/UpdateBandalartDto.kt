package com.nexters.bandalart.android.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class BandalartCellWithChildrenDto(
  @Embedded
  val cell: BandalartCellEntity,

  @Relation(
    parentColumn = "id",
    entityColumn = "parentId"
  )
  val children: List<BandalartCellEntity>
)

@Serializable
data class UpdateBandalartMainCellDto(
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
data class UpdateBandalartSubCellDto(
  @SerialName("title")
  val title: String?,
  @SerialName("description")
  val description: String?,
  @SerialName("dueDate")
  val dueDate: String?,
)

@Serializable
data class UpdateBandalartTaskCellDto(
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
data class UpdateBandalartEmojiDto(
  @SerialName("profileEmoji")
  val profileEmoji: String?,
)
