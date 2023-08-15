package com.nexters.bandalart.android.core.data.mapper

import com.nexters.bandalart.android.core.data.model.bandalart.BandalartDetailResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartCellResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartShareResponse
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartEmojiRequest
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartMainCellRequest
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartSubCellRequest
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartTaskCellRequest
import com.nexters.bandalart.android.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartShareEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartMainCellEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartSubCellEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartTaskCellEntity

internal fun BandalartResponse.toEntity() =
  BandalartEntity(
    key = key,
    mainColor = mainColor,
    subColor = subColor,
    profileEmoji = profileEmoji,
    completionRatio = completionRatio,
  )

internal fun BandalartDetailResponse.toEntity() =
  BandalartDetailEntity(
    key = key,
    mainColor = mainColor,
    subColor = subColor,
    profileEmoji = profileEmoji,
    title = title,
    cellKey = cellKey,
    dueDate = dueDate,
    isCompleted = isCompleted,
    completionRatio = completionRatio,
  )

// 재귀 호출시 컴파일러가 타입을 추론할 수 없기 때문에 예외적으로 반환타입을 지정
internal fun BandalartCellResponse.toEntity(): BandalartCellEntity {
  return BandalartCellEntity(
    key = key,
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
    completionRatio = completionRatio,
    profileEmoji = profileEmoji,
    mainColor = mainColor,
    subColor = subColor,
    parentKey = parentKey,
    children = children.map { it.toEntity() },
  )
}

internal fun UpdateBandalartMainCellEntity.toModel() =
  UpdateBandalartMainCellRequest(
    title = title,
    description = description,
    dueDate = dueDate,
    profileEmoji = profileEmoji,
    mainColor = mainColor,
    subColor = subColor,
  )

internal fun UpdateBandalartSubCellEntity.toModel() =
  UpdateBandalartSubCellRequest(
    title = title,
    description = description,
    dueDate = dueDate,
  )

internal fun UpdateBandalartTaskCellEntity.toModel() =
  UpdateBandalartTaskCellRequest(
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
  )

internal fun UpdateBandalartEmojiEntity.toModel() =
  UpdateBandalartEmojiRequest(profileEmoji = profileEmoji)

internal fun BandalartShareResponse.toEntity() =
  BandalartShareEntity(
    shareUrl = shareUrl,
    key = key,
    endDate = endDate,
  )
