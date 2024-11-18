package com.nexters.bandalart.android.core.data.mapper

import com.nexters.bandalart.android.core.database.entity.BandalartDBEntity
import com.nexters.bandalart.android.core.database.entity.BandalartDetailDBEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartDetailEntity
import com.nexters.bandalart.android.core.domain.entity.BandalartEntity

// DB Entity -> Domain Entity
fun BandalartDBEntity.toEntity() = BandalartEntity(
  id = id,
  mainColor = mainColor,
  subColor = subColor,
  profileEmoji = profileEmoji,
  completionRatio = completionRatio
)

// Domain Entity -> DB Entity
fun BandalartEntity.toDBEntity() = BandalartDBEntity(
  id = id,
  mainColor = mainColor,
  subColor = subColor,
  profileEmoji = profileEmoji,
  completionRatio = completionRatio
)

// DB Entity -> Domain Entity
fun BandalartDetailDBEntity.toEntity() = BandalartDetailEntity(
  id = id,
  mainColor = mainColor,
  subColor = subColor,
  profileEmoji = profileEmoji,
  title = title,
  cellId = cellId,
  dueDate = dueDate,
  isCompleted = isCompleted,
  completionRatio = completionRatio
)
