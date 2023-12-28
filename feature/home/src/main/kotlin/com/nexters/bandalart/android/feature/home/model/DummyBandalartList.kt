package com.nexters.bandalart.android.feature.home.model

import com.nexters.bandalart.android.core.ui.allColor
import kotlinx.datetime.Clock

val dummyBandalartList = listOf(
  BandalartDetailUiModel(
    key = "6km1Z",
    mainColor = allColor[0].mainColor,
    subColor = allColor[0].subColor,
    profileEmoji = "😎",
    title = "발전하는 예진",
    cellKey = "",
    dueDate = Clock.System.now().toString(),
    isCompleted = false,
    completionRatio = 88,
    isGeneratedTitle = false,
  ),
  BandalartDetailUiModel(
    key = "9nyXk",
    mainColor = allColor[1].mainColor,
    subColor = allColor[1].subColor,
    profileEmoji = "🔥",
    title = "발전하는 석규",
    cellKey = "",
    dueDate = Clock.System.now().toString(),
    isCompleted = false,
    completionRatio = 66,
    isGeneratedTitle = false,
  ),
  BandalartDetailUiModel(
    key = "WSl7R",
    mainColor = allColor[2].mainColor,
    subColor = allColor[2].subColor,
    profileEmoji = "❤️‍🔥",
    title = "발전하는 지훈",
    cellKey = "",
    dueDate = Clock.System.now().toString(),
    isCompleted = false,
    completionRatio = 44,
    isGeneratedTitle = false,
  ),
  BandalartDetailUiModel(
    key = "5z1EG",
    mainColor = allColor[3].mainColor,
    subColor = allColor[3].subColor,
    profileEmoji = "💛",
    title = "발전하는 인혁",
    cellKey = "",
    dueDate = Clock.System.now().toString(),
    isCompleted = false,
    completionRatio = 22,
    isGeneratedTitle = false,
  ),
)
