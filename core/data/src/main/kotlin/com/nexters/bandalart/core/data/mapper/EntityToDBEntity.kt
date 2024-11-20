package com.nexters.bandalart.core.data.mapper

import com.nexters.bandalart.core.database.entity.BandalartCellDBEntity
import com.nexters.bandalart.core.database.entity.BandalartDBEntity
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.entity.BandalartEntity

fun BandalartEntity.toDBEntity() = BandalartDBEntity(
    id = id,
    mainColor = mainColor,
    subColor = subColor,
    profileEmoji = profileEmoji,
    completionRatio = completionRatio,
)

fun BandalartCellEntity.toDBEntity() = BandalartCellDBEntity(
    id = id,
    bandalartId = bandalartId,
    title = title,
    description = description,
    dueDate = dueDate,
    isCompleted = isCompleted,
    parentId = parentId,
)

