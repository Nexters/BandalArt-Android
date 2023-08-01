package com.nexters.bandalart.android.feature.home.mapper

import com.nexters.bandalart.android.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel

internal fun BandalartCellEntity.toUiModel(): BandalartCellUiModel {
  return BandalartCellUiModel(
    key = key,
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
    completionRatio = completionRatio,
    parentKey = parentKey,
    children = children.map { it.toUiModel() },
  )
}
