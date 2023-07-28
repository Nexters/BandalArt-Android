package com.nexters.bandalart.android.core.domain.entity

data class BandalartListEntity(
  val key: String,
  val cellKey: String,
  val dueDate: String,
  val isCompleted: Boolean,
  val shareKey: String,
)
