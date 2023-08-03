package com.nexters.bandalart.android.core.domain.entity

/**
 * 반다라트 상세 조회
 *
 * @param key 반다라트 고유 키
 * @param mainColor 반다라트 메인 색상
 * @param subColor 반다라트 서브 색상
 * @param profileEmoji 반다라트 프로필 이모지
 * @param title 반다라트 메인 목표
 * @param cellKey 반다라트 메인 셀 고유 키
 * @param dueDate 반다라트 마감일, 미설정인 경우 null
 * @param isCompleted 반다라트 완료 여부 (main cell 완료 여부와 같음)
 * @param shareKey 반다라트 웹게시 공유 키, 공유 하지 않은 경우 null
 */

data class BandalartDetailEntity(
  val key: String,
  val mainColor: String,
  val subColor: String,
  val profileEmoji: String?,
  val title: String?,
  val cellKey: String,
  val dueDate: String?,
  val isCompleted: Boolean,
  val shareKey: String?,
)
