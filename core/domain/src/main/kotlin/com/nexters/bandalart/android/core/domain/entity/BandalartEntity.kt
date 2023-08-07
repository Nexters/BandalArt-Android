package com.nexters.bandalart.android.core.domain.entity

/**
 * 반다라트 생성
 *
 * @param id
 * @param key 셀 고유 키
 * @param mainColor 메인 테마 색상 (Sub Cell, Task Cell 이면 null)
 * @param subColor 서브 테마 색상 (Sub Cell, Task Cell 이면 null)
 * @param profileEmoji 프로필 이모지 (Sub Cell, Task Cell 이면 null)
 * @param cellId
 * @param sharedId
 * @param userId
 * @param createdAt 생성된 시각
 * @param updatedAt 수정된 시각
 * @param deleteAt  삭제된 시각
 */

data class BandalartEntity(
  val id: String,
  val key: String,
  val mainColor: String,
  val subColor: String,
  val profileEmoji: String?,
  val cellId: Int,
  val sharedId: String?,
  val userId: Int,
  val createdAt: String,
  val updatedAt: String,
  val deleteAt: String?,
)
