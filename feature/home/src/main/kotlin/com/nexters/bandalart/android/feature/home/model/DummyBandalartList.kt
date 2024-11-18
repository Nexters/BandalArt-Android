package com.nexters.bandalart.android.feature.home.model

import com.nexters.bandalart.android.core.ui.allColor
import kotlinx.datetime.Clock

val dummyBandalartList = listOf(
  BandalartDetailUiModel(
    id = 0L,
    mainColor = allColor[0].mainColor,
    subColor = allColor[0].subColor,
    profileEmoji = "😎",
    title = "발전하는 예진",
    cellId = 0L,
    dueDate = Clock.System.now().toString(),
    isCompleted = false,
    completionRatio = 88,
    isGeneratedTitle = false,
  ),
  BandalartDetailUiModel(
    id = 0L,
    mainColor = allColor[1].mainColor,
    subColor = allColor[1].subColor,
    profileEmoji = "🔥",
    title = "발전하는 석규",
    cellId = 0L,
    dueDate = Clock.System.now().toString(),
    isCompleted = false,
    completionRatio = 66,
    isGeneratedTitle = false,
  ),
  BandalartDetailUiModel(
    id = 0L,
    mainColor = allColor[2].mainColor,
    subColor = allColor[2].subColor,
    profileEmoji = "❤️‍🔥",
    title = "발전하는 지훈",
    cellId = 0L,
    dueDate = Clock.System.now().toString(),
    isCompleted = false,
    completionRatio = 44,
    isGeneratedTitle = false,
  ),
  BandalartDetailUiModel(
    id = 0L,
    mainColor = allColor[3].mainColor,
    subColor = allColor[3].subColor,
    profileEmoji = "💛",
    title = "발전하는 인혁",
    cellId = 0L,
    dueDate = Clock.System.now().toString(),
    isCompleted = false,
    completionRatio = 22,
    isGeneratedTitle = false,
  ),
)
