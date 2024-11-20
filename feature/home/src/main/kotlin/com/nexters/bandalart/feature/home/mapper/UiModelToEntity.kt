package com.nexters.bandalart.feature.home.mapper

import com.nexters.bandalart.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartMainCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartSubCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartTaskCellEntity
import com.nexters.bandalart.feature.home.model.UpdateBandalartEmojiModel
import com.nexters.bandalart.feature.home.model.UpdateBandalartMainCellModel
import com.nexters.bandalart.feature.home.model.UpdateBandalartSubCellModel
import com.nexters.bandalart.feature.home.model.UpdateBandalartTaskCellModel

internal fun UpdateBandalartEmojiModel.toEntity() =
    UpdateBandalartEmojiEntity(profileEmoji = profileEmoji)

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
