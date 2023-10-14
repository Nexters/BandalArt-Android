package com.nexters.bandalart.android.feature.home.ui.bandalart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.component.CellText
import com.nexters.bandalart.android.core.ui.extension.ThemeColor
import com.nexters.bandalart.android.core.ui.extension.toColor
import com.nexters.bandalart.android.core.designsystem.theme.Gray200
import com.nexters.bandalart.android.core.designsystem.theme.Gray400
import com.nexters.bandalart.android.core.designsystem.theme.Gray500
import com.nexters.bandalart.android.core.designsystem.theme.Gray900
import com.nexters.bandalart.android.core.designsystem.theme.White
import com.nexters.bandalart.android.feature.home.BandalartBottomSheet
import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel

data class CellInfo(
  val isSubCell: Boolean = false,
  val colIndex: Int = 2,
  val rowIndex: Int = 2,
  val colCnt: Int = 1,
  val rowCnt: Int = 1,
)

data class SubCell(
  val rowCnt: Int,
  val colCnt: Int,
  val subCellRowIndex: Int,
  val subCellColIndex: Int,
  val bandalartChartData: BandalartCellUiModel?,
)

@Composable
fun BandalartCell(
  modifier: Modifier = Modifier,
  bandalartKey: String,
  themeColor: ThemeColor,
  isMainCell: Boolean,
  cellInfo: CellInfo = CellInfo(),
  cellData: BandalartCellUiModel,
  bottomSheetDataChanged: (Boolean) -> Unit,
  outerPadding: Dp = 3.dp,
  innerPadding: Dp = 2.dp,
  mainCellPadding: Dp = 1.dp,
) {
  var openBottomSheet by rememberSaveable { mutableStateOf(false) }
  val backgroundColor = when {
    isMainCell -> themeColor.mainColor.toColor()
    cellInfo.isSubCell and cellData.isCompleted -> themeColor.subColor.toColor().copy(alpha = 0.6f)
    cellInfo.isSubCell and !cellData.isCompleted -> themeColor.subColor.toColor()
    cellData.isCompleted -> Gray200
    else -> White
  }
  Box(
    modifier = modifier
      .padding(
        start = if (isMainCell) mainCellPadding else if (cellInfo.colIndex == 0) outerPadding else innerPadding,
        end = if (isMainCell) mainCellPadding else if (cellInfo.colIndex == cellInfo.colCnt - 1) outerPadding else innerPadding,
        top = if (isMainCell) mainCellPadding else if (cellInfo.rowIndex == 0) outerPadding else innerPadding,
        bottom = if (isMainCell) mainCellPadding else if (cellInfo.rowIndex == cellInfo.rowCnt - 1) outerPadding else innerPadding,
      )
      .aspectRatio(1f)
      .clip(RoundedCornerShape(10.dp))
      .background(backgroundColor)
      .clickable { openBottomSheet = !openBottomSheet },
    contentAlignment = Alignment.Center,
  ) {
    // 메인 목표
    if (isMainCell) {
      val cellTextColor = themeColor.subColor.toColor()
      // 메인 목표가 빈 경우
      if (cellData.title.isNullOrEmpty()) {
        Box(contentAlignment = Alignment.Center) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CellText(
              cellText = stringResource(R.string.home_main_cell),
              cellTextColor = cellTextColor,
              fontWeight = FontWeight.W700,
            )
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = stringResource(R.string.add_descrption),
              tint = cellTextColor,
              modifier = Modifier
                .size(20.dp)
                .offset(y = (-4).dp),
            )
          }
        }
      } else {
        CellText(
          cellText = cellData.title,
          cellTextColor = cellTextColor,
          fontWeight = FontWeight.W700,
        )
      }
      // 서브 목표
    } else if (cellInfo.isSubCell) {
      val cellTextColor = themeColor.mainColor.toColor()
      val fontWeight = FontWeight.W700
      // 서브 목표가 빈 경우
      if (cellData.title.isNullOrEmpty()) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center,
        ) {
          CellText(
            cellText = stringResource(R.string.home_sub_cell),
            cellTextColor = cellTextColor,
            fontWeight = fontWeight,
          )
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_descrption),
            tint = cellTextColor,
            modifier = Modifier
              .size(20.dp)
              .offset(y = (-4).dp),
          )
        }
      } else {
        // 서브 목표를 달성할 경우
        CellText(
          cellText = cellData.title,
          cellTextColor = cellTextColor,
          fontWeight = fontWeight,
          textAlpha = if (cellData.isCompleted) 0.6f else 1f,
        )
      }
    } else {
      // 테스크
      val cellTextColor = if (cellData.isCompleted) Gray400 else Gray900
      val fontWeight = FontWeight.W500

      // 테스크가 비어있는 경우
      if (cellData.title.isNullOrEmpty()) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = stringResource(R.string.add_descrption),
          tint = Gray500,
          modifier = Modifier.size(20.dp),
        )
      } else {
        // 테스크의 목표를 달성한 경우
        if (cellData.isCompleted) {
          Box(
            modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
          ) {
            CellText(
              cellText = cellData.title,
              cellTextColor = cellTextColor,
              fontWeight = fontWeight,
            )
            val image = painterResource(
              id = com.nexters.bandalart.android.core.designsystem.R.drawable.ic_cell_check,
            )
            Image(
              painter = image,
              contentDescription = stringResource(R.string.complete_descrption),
              modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-4).dp, y = (-4).dp),
            )
          }
        } else {
          CellText(
            cellText = cellData.title,
            cellTextColor = cellTextColor,
            fontWeight = fontWeight,
          )
        }
      }
    }
    if (openBottomSheet) {
      BandalartBottomSheet(
        bandalartKey = bandalartKey,
        isSubCell = cellInfo.isSubCell,
        isMainCell = isMainCell,
        isBlankCell = cellData.title.isNullOrEmpty(),
        cellData = cellData,
        onResult = { bottomSheetState, bottomSheetDataChangedState ->
          openBottomSheet = bottomSheetState
          bottomSheetDataChanged(bottomSheetDataChangedState)
        },
      )
    }
  }
}
