package com.nexters.bandalart.feature.home.mapper

import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.feature.home.model.BandalartDetailUiModel

internal fun BandalartDetailEntity.toUiModel() =
    BandalartDetailUiModel(
        id = id,
        mainColor = mainColor,
        subColor = subColor,
        profileEmoji = profileEmoji,
        title = title,
        dueDate = dueDate,
        isCompleted = isCompleted,
        completionRatio = completionRatio,
        isGeneratedTitle = false,
    )

// 재귀 호출이라 반환 타입 명시하지 않으면 에러 발생
fun BandalartCellEntity.toUiModel(): BandalartCellUiModel {
    return BandalartCellUiModel(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        isCompleted = isCompleted,
        completionRatio = completionRatio,
        profileEmoji = profileEmoji,
        mainColor = mainColor,
        subColor = subColor,
        parentId = parentId,
        children = children.map { childEntity -> childEntity.toUiModel() }
    )
}


