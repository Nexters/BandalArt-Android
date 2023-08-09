package com.nexters.bandalart.android.feature.home.model

data class BandalartDetailUiModel(
  val key: String = "",
  val mainColor: String = "#3FFFBA",
  val subColor: String = "#111827",
  val profileEmoji: String? = "",
  val title: String? = "",
  val cellKey: String = "",
  val dueDate: String? = "",
  val isCompleted: Boolean = false,
  val completionRatio: Int = 0,
)
