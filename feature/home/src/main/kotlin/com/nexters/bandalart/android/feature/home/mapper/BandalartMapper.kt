package com.nexters.bandalart.android.feature.home.mapper

import com.nexters.bandalart.android.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartMainCellEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartSubCellEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartTaskCellEntity
import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.android.feature.home.model.BandalartDetailUiModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartMainCellModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartSubCellModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartTaskCellModel

internal fun BandalartDetailEntity.toUiModel() =
  BandalartDetailUiModel(
    key = key,
    cellKey = cellKey,
    mainColor = mainColor,
    subColor = subColor,
    profileEmoji = profileEmoji,
    title = title,
    dueDate = dueDate,
    isCompleted = isCompleted,
    shareKey = shareKey,
  )

// 재귀 호출시 컴파일러가 타입을 추론할 수 없기 때문에 예외적으로 반환타입을 지정
internal fun BandalartCellEntity.toUiModel(): BandalartCellUiModel {
  return BandalartCellUiModel(
    key = key,
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
    profileEmoji = profileEmoji,
    mainColor = mainColor,
    subColor = subColor,
    completionRatio = completionRatio,
    parentKey = parentKey,
    children = children.map { it.toUiModel() },
  )
}

internal fun UpdateBandalartMainCellModel.toEntity() =
  UpdateBandalartMainCellEntity(
    title = title,
    description = description,
    dueDate = dueDate,
    emoji = emoji,
    mainColor = mainColor,
    subColor = subColor,
  )

internal fun UpdateBandalartSubCellModel.toEntity() =
  UpdateBandalartSubCellEntity(
    title = title,
    description = description,
    dueDate = dueDate,
  )

internal fun UpdateBandalartTaskCellModel.toEntity() =
  UpdateBandalartTaskCellEntity(
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
  )
