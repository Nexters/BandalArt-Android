package com.nexters.bandalart.feature.home.mapper

import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartMainCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartSubCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartTaskCellEntity
import com.nexters.bandalart.feature.home.model.UpdateBandalartMainCellModel
import com.nexters.bandalart.feature.home.model.UpdateBandalartSubCellModel
import com.nexters.bandalart.feature.home.model.UpdateBandalartTaskCellModel
import com.nexters.bandalart.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.feature.home.model.BandalartDetailUiModel

internal fun BandalartDetailEntity.toUiModel() =
    BandalartDetailUiModel(
        id = id,
        cellId = cellId,
        mainColor = mainColor,
        subColor = subColor,
        profileEmoji = profileEmoji,
        title = title,
        dueDate = dueDate,
        isCompleted = isCompleted,
        completionRatio = completionRatio,
        isGeneratedTitle = false,
    )

fun BandalartCellEntity.toUiModel() = BandalartCellUiModel(
    id = id ?: 0,
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
    completionRatio = completionRatio,
    profileEmoji = profileEmoji,
    mainColor = mainColor,
    subColor = subColor,
    parentId = parentId,
)

internal fun UpdateBandalartMainCellModel.toEntity() =
    UpdateBandalartMainCellEntity(
        title = title,
        description = description,
        dueDate = dueDate,
        profileEmoji = profileEmoji,
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

internal fun com.nexters.bandalart.feature.home.model.UpdateBandalartEmojiModel.toEntity() =
    UpdateBandalartEmojiEntity(profileEmoji = profileEmoji)
