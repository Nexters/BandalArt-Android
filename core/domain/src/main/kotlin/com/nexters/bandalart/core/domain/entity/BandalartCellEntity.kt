package com.nexters.bandalart.core.domain.entity

/**
 * 반다라트 셀 조회
 *
 * @param id 셀 고유 id
 * @param title 셀 제목
 * @param description 셀 설명
 * @param dueDate 셀 마감일, 미설정인 경우 null
 * @param isCompleted 셀 완료 여부
 * @param parentId 부모 셀 고유 키 (Main Cell 이면 null)
 */

data class BandalartCellEntity(
    val id: Long,
    val bandalartId: Long,
    val title: String?,
    val description: String?,
    val dueDate: String?,
    val isCompleted: Boolean,
    val parentId: Long?,
    val children: List<BandalartCellEntity> = emptyList()
)
