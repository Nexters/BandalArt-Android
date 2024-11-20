package com.nexters.bandalart.feature.home.model

import com.nexters.bandalart.core.ui.allColor
import kotlinx.datetime.Clock

val dummyBandalartData = BandalartUiModel(
    id = 0L,
    mainColor = allColor[0].mainColor,
    subColor = allColor[0].subColor,
    profileEmoji = "😎",
    title = "발전하는 예진",
    cellId = 0L,
    dueDate = Clock.System.now().toString(),
    isCompleted = false,
    completionRatio = 66,
    isGeneratedTitle = false,
)
