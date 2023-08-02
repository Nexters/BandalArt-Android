package com.nexters.bandalart.android.feature.home.model

data class BandalartDetailUiModel(
  val key: String = "",
  val mainColor: String = "",
  val subColor: String = "",
  val profileEmoji: String? = "",
  val title: String?,
  val cellKey: String = "",
  val dueDate: String? = "",
  val isCompleted: Boolean = false,
  val shareKey: String? = "",
)
