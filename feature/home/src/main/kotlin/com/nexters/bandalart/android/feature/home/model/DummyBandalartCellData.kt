package com.nexters.bandalart.android.feature.home.model

import com.nexters.bandalart.android.core.ui.allColor

val dummyBandalartCellData = BandalartCellUiModel(
  id = 0L,
  title = "Android 출시",
  description = null,
  isCompleted = true,
  completionRatio = 100,
  profileEmoji = "😎",
  mainColor = allColor[0].mainColor,
  subColor = allColor[0].subColor,
  parentId = 0L,
)
