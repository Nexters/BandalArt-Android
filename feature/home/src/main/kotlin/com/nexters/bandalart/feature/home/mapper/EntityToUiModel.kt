package com.nexters.bandalart.feature.home.mapper

import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.entity.BandalartEntity
import com.nexters.bandalart.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.feature.home.model.BandalartUiModel

fun BandalartEntity.toUiModel() = BandalartUiModel(
    id = id,
    mainColor = mainColor,
    subColor = subColor,
    profileEmoji = profileEmoji,
    title = title,
    dueDate = dueDate,
    isCompleted = isCompleted,
    completionRatio = completionRatio
)

// 재귀 호출이라 반환 타입 명시하지 않으면 에러 발생
fun BandalartCellEntity.toUiModel(): BandalartCellUiModel {
    return BandalartCellUiModel(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        isCompleted = isCompleted,
        parentId = parentId,
        children = children.map { childEntity -> childEntity.toUiModel() }
    )
}


