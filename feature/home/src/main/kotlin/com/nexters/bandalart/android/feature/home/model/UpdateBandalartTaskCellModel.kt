package com.nexters.bandalart.android.feature.home.model

data class UpdateBandalartTaskCellModel(
  val title: String? = "",
  val description: String? = null,
  val dueDate: String? = null,
  val isCompleted: Boolean? = null,
)
