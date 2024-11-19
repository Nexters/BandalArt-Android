package com.nexters.bandalart.core.domain.entity

/**
 * 반다라트 상세 조회
 *
 * @param id 반다라트 고유 id
 * @param mainColor 반다라트 메인 색상
 * @param subColor 반다라트 서브 색상
 * @param profileEmoji 반다라트 프로필 이모지
 * @param title 반다라트 메인 목표
 * @param cellId 반다라트 메인 셀 고유 id
 * @param dueDate 반다라트 마감일, 미설정인 경우 null
 * @param isCompleted 반다라트 완료 여부 (main cell 완료 여부와 같음)
 */

data class BandalartDetailEntity(
    val id: Long,  // non-null (항상 Bandalart의 id 있음)
    val mainColor: String,
    val subColor: String,
    val profileEmoji: String?,
    val title: String?,
    val cellId: Long,  // non-null (항상 메인셀 존재)
    val dueDate: String?,
    val isCompleted: Boolean,
    val completionRatio: Int,
)
