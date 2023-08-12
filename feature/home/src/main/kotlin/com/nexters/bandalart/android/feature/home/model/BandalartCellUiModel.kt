package com.nexters.bandalart.android.feature.home.model

data class BandalartCellUiModel(
  val key: String = "",
  val title: String? = null,
  val description: String? = null,
  val dueDate: String? = null,
  val isCompleted: Boolean = false,
  val completionRatio: Int = 0,
  val profileEmoji: String? = "",
  val mainColor: String? = "",
  val subColor: String? = "",
  val parentKey: String? = "",
  val children: List<BandalartCellUiModel> = emptyList(),
) {
  fun copy(): BandalartCellUiModel {

    return BandalartCellUiModel(
      key,
      title,
      description,
      dueDate,
      isCompleted,
      completionRatio,
      profileEmoji,
      mainColor,
      subColor,
      parentKey,
      children
    )
  }
}
