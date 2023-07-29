package com.nexters.bandalart.android.core.domain.entity

/**
 * 반다라트 목록 조회
 *
 * @param key 반다라트 고유 키
 * @param cellKey 메인 셀 고유 키
 * @param dueDate 반다라트 마감일, 미설정인 경우 null
 * @param isCompleted 반다라트 완료 여부 (main cell 완료 여부와 같음)
 * @param shareKey 반다라트 웹게시 공유 키, 공유 하지 않은 경우 null
 */

data class BandalartListEntity(
  val key: String,
  val cellKey: String,
  val dueDate: String,
  val isCompleted: Boolean,
  val shareKey: String,
)
