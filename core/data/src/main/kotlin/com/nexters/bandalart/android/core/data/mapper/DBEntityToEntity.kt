package com.nexters.bandalart.android.core.data.mapper

import com.nexters.bandalart.android.core.database.entity.BandalartCellDBEntity
import com.nexters.bandalart.android.core.database.entity.BandalartDBEntity
import com.nexters.bandalart.android.core.database.entity.BandalartDetailDBEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartEntity

fun BandalartDBEntity.toEntity() = BandalartEntity(
    id = id,
    mainColor = mainColor,
    subColor = subColor,
    profileEmoji = profileEmoji,
    completionRatio = completionRatio,
)

fun BandalartDetailDBEntity.toEntity() = BandalartDetailEntity(
    id = id,
    mainColor = mainColor,
    subColor = subColor,
    profileEmoji = profileEmoji,
    title = title,
    cellId = cellId,
    dueDate = dueDate,
    isCompleted = isCompleted,
    completionRatio = completionRatio,
)

fun BandalartCellDBEntity.toEntity(): BandalartCellEntity = BandalartCellEntity(
    id = id,
    bandalartId = bandalartId,
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
