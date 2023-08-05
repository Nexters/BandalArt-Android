package com.nexters.bandalart.android.feature.home.model

data class BandalartCellUiModel(
  val key: String = "",
  val title: String? = "",
  val description: String? = "",
  val dueDate: String? = "",
  val isCompleted: Boolean = false,
  val completionRatio: Int = 0,
  val profileEmoji: String? = "",
  val mainColor: String? = "",
  val subColor: String? = "",
  val parentKey: String? = "",
  val children: List<BandalartCellUiModel> = emptyList(),
)
