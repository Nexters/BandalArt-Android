package com.nexters.bandalart.android.core.data.mapper

import com.nexters.bandalart.android.core.data.model.bandalart.BandalartDetailResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartListResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartCellResponse
import com.nexters.bandalart.android.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartListEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartCellEntity

internal fun BandalartListResponse.toEntity() =
  BandalartListEntity(
    key = key,
    cellKey = cellKey,
    dueDate = dueDate,
    isCompleted = isCompleted,
    shareKey = shareKey,
  )

internal fun BandalartDetailResponse.toEntity() =
  BandalartDetailEntity(
    key = key,
    cellKey = cellKey,
    dueDate = dueDate,
    isCompleted = isCompleted,
    shareKey = shareKey,
  )

// 재귀 호출시 컴파일러가 타입을 추론할 수 없기 때문에 예외적으로 반환타입을 지정
internal fun BandalartCellResponse.toEntity(): BandalartCellEntity {
  return BandalartCellEntity(
    key = key,
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
    parentKey = parentKey,
    children = children.map { it.toEntity() },
  )
}