package com.nexters.bandalart.android.core.domain.entity

/**
 * 반다라트 셀 조회
 *
 * @param key 셀 고유 키
 * @param title 셀 제목
 * @param description 셀 설명
 * @param dueDate 셀 마감일, 미설정인 경우 null
 * @param isCompleted 셀 완료 여부
 * @param completionRatio 달성률(%)
 * @param parentKey 부모 셀 고유 키(Main Cell 이면 null)
 * @param children 하위 셀 목록(Task Cell 이면 빈 배열)
 */

data class BandalartCellEntity(
  val key: String,
  val title: String?,
  val description: String?,
  val dueDate: String?,
  val isCompleted: Boolean,
  val completionRatio: Int,
  val parentKey: String?,
  val children: List<BandalartCellEntity>,
)
