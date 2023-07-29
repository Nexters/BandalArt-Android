package com.nexters.bandalart.android.feature.home.model

data class BandalartMainCellUiModel(
  val key: String = "",
  val title: String = "",
  val description: String? = "",
  val dueDate: String? = "",
  val isCompleted: Boolean = false,
  val parentKey: String? = "",
  val children: List<BandalartMainCellUiModel> = emptyList(),
)
