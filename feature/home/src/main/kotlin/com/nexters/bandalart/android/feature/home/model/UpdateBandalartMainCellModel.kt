package com.nexters.bandalart.android.feature.home.model

data class UpdateBandalartMainCellModel(
  val title: String = "",
  val description: String? = null,
  val dueDate: String? = null,
  val emoji: String? = "",
  val mainColor: String,
  val subColor: String,
)
