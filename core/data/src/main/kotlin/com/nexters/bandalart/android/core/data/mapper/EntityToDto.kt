package com.nexters.bandalart.android.core.data.mapper

import com.nexters.bandalart.android.core.database.entity.UpdateBandalartEmojiDto
import com.nexters.bandalart.android.core.database.entity.UpdateBandalartMainCellDto
import com.nexters.bandalart.android.core.database.entity.UpdateBandalartSubCellDto
import com.nexters.bandalart.android.core.database.entity.UpdateBandalartTaskCellDto
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartMainCellEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartSubCellEntity
import com.nexters.bandalart.android.core.domain.entity.UpdateBandalartTaskCellEntity

fun UpdateBandalartMainCellEntity.toDto(): UpdateBandalartMainCellDto = UpdateBandalartMainCellDto(
    title = title,
    description = description,
    dueDate = dueDate,
    profileEmoji = profileEmoji,
    mainColor = mainColor,
    subColor = subColor,
)

fun UpdateBandalartSubCellEntity.toDto(): UpdateBandalartSubCellDto = UpdateBandalartSubCellDto(
    title = title,
    description = description,
    dueDate = dueDate,
)

fun UpdateBandalartTaskCellEntity.toDto(): UpdateBandalartTaskCellDto = UpdateBandalartTaskCellDto(
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
)

fun UpdateBandalartEmojiEntity.toDto(): UpdateBandalartEmojiDto = UpdateBandalartEmojiDto(
    profileEmoji = profileEmoji,
)
