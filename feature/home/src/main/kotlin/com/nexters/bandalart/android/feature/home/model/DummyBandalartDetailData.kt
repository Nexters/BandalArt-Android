package com.nexters.bandalart.android.feature.home.model

import com.nexters.bandalart.android.core.ui.allColor
import kotlinx.datetime.Clock

val dummyBandalartDetailData = BandalartDetailUiModel(
  key = "",
  mainColor = allColor[0].mainColor,
  subColor = allColor[0].subColor,
  profileEmoji = "😎",
  title = "발전하는 예진",
  cellKey = "",
  dueDate = Clock.System.now().toString(),
  isCompleted = false,
  completionRatio = 66,
  isGeneratedTitle = false,
)
