package com.nexters.bandalart.android.feature.home.model

data class BandalartCellUiModel(
  val id: Long = 0L,
  val title: String? = null,
  val description: String? = null,
  val dueDate: String? = null,
  val isCompleted: Boolean = false,
  val completionRatio: Int = 0,
  val profileEmoji: String? = "",
  val mainColor: String? = "",
  val subColor: String? = "",
  val parentId: Long? = 0L,
) {
  fun copy(): BandalartCellUiModel = BandalartCellUiModel(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
    completionRatio = completionRatio,
    profileEmoji = profileEmoji,
    mainColor = mainColor,
    subColor = subColor,
    parentId = parentId,
  )
}
