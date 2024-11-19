package com.nexters.bandalart.core.data.mapper

import com.nexters.bandalart.core.database.entity.BandalartCellDBEntity
import com.nexters.bandalart.core.database.entity.BandalartDBEntity
import com.nexters.bandalart.core.database.entity.BandalartDetailDBEntity
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.core.domain.entity.BandalartEntity

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
    dueDate = dueDate,
    isCompleted = isCompleted,
    completionRatio = completionRatio,
)

fun BandalartCellDBEntity.toEntity() = BandalartCellEntity(
    id = id ?: 0L,
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
