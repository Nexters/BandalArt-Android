package com.nexters.bandalart.android.core.domain.entity

data class BandalartMainCellEntity(
  val key: String,
  val title: String,
  val description: String?,
  val dueDate: String?,
  val isCompleted: Boolean,
  val parentKey: String?,
  val children: List<BandalartMainCellEntity>,
)
