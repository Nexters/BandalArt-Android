package com.nexters.bandalart.android.core.domain.entity

/**
 * 반다라트 셀 조회
 *
 * @param key 셀 고유 키
 * @param title 셀 제목
 * @param description 셀 설명
 * @param dueDate 셀 마감일, 미설정인 경우 null
 * @param isCompleted 셀 완료 여부
 * @param completionRatio 목표 달성률(%)
 * @param profileEmoji 프로필 이모지 (Sub Cell, Task Cell 이면 null)
 * @param mainColor 메인 테마 색상 (Sub Cell, Task Cell 이면 null)
 * @param subColor 서브 테마 색상 (Sub Cell, Task Cell 이면 null)
 * @param parentKey 부모 셀 고유 키 (Main Cell 이면 null)
 * @param children 하위 셀 목록(Task Cell 이면 빈 배열)
 */

data class BandalartCellEntity(
  val key: String,
  val title: String?,
  val description: String?,
  val dueDate: String?,
  val isCompleted: Boolean,
  val completionRatio: Int,
  val profileEmoji: String?,
  val mainColor: String?,
  val subColor: String?,
  val parentKey: String?,
  val children: List<BandalartCellEntity>,
)
