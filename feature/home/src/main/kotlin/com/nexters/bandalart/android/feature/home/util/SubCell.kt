package com.nexters.bandalart.android.feature.home.util

import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel

data class SubCell(
  val rowCnt: Int,
  val colCnt: Int,
  val subCellRowIndex: Int,
  val subCellColIndex: Int,
  val bandalartChartData: BandalartCellUiModel,
)
