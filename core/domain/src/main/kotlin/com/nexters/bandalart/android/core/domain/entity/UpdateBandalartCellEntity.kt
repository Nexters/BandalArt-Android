package com.nexters.bandalart.android.core.domain.entity

/**
 * 반다라트 셀 수정
 *
 * @param title 셀 제목
 * @param description 셀 설명
 * @param dueDate 셀 마감일, 미설정인 경우 null
 * @param isCompleted 셀 완료 여부
 */

data class UpdateBandalartCellEntity(
  val title: String,
  val description: String?,
  val dueDate: String?,
  val isCompleted: Boolean?,
)
