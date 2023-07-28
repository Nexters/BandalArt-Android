package com.nexters.bandalart.android.feature.home.mapper

import com.nexters.bandalart.android.core.domain.entity.BandalartMainCellEntity
import com.nexters.bandalart.android.feature.home.model.BandalartMainCellUiModel

internal fun BandalartMainCellEntity.toUiModel(): BandalartMainCellUiModel {
  return BandalartMainCellUiModel(
    key = key,
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
    parentKey = parentKey,
    children = children.map { it.toUiModel() },
  )
}
