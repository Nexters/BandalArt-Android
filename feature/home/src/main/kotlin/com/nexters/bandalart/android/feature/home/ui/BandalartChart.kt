package com.nexters.bandalart.android.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.extension.ThemeColor
import com.nexters.bandalart.android.core.ui.extension.toColor
import com.nexters.bandalart.android.core.ui.theme.Gray100
import com.nexters.bandalart.android.core.ui.theme.MainColor
import com.nexters.bandalart.android.feature.home.HomeUiState
import com.nexters.bandalart.android.feature.home.util.SubCell

@Composable
fun BandalartChart(
  modifier: Modifier = Modifier,
  bandalartKey: String,
  uiState: HomeUiState,
  themeColor: ThemeColor,
  bottomSheetDataChanged: (Boolean) -> Unit,
) {
  val context = LocalContext.current
  val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
  val paddedMaxWidth = remember(screenWidthDp) {
    screenWidthDp - (15.dp * 2)
  }

  val subCellList = listOf(
    SubCell(2, 3, 1, 1, uiState.bandalartCellData!!.children[0]),
    SubCell(3, 2, 1, 0, uiState.bandalartCellData.children[1]),
    SubCell(3, 2, 1, 1, uiState.bandalartCellData.children[2]),
    SubCell(2, 3, 0, 1, uiState.bandalartCellData.children[3]),
  )

  Layout(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp)),
    content = {
      for (index in subCellList.indices) {
        Box(
          modifier
            .layoutId(context.getString(R.string.homescreen_layout_id_text, index + 1))
            .clip(RoundedCornerShape(12.dp))
            .background(color = Gray100),
          content = {
            BandalartCellGrid(
              bandalartKey = bandalartKey,
              themeColor = themeColor,
              subCell = subCellList[index],
              rows = subCellList[index].rowCnt,
              cols = subCellList[index].colCnt,
              bottomSheetDataChanged = bottomSheetDataChanged,
            )
          },
        )
      }
      Box(
        modifier
          .layoutId(context.getString(R.string.homescreen_main_id_text))
          .clip(RoundedCornerShape(10.dp))
          .background(color = (uiState.bandalartCellData.mainColor?.toColor() ?: MainColor)),
        content = {
          BandalartCell(
            isMainCell = true,
            themeColor = themeColor,
            cellData = uiState.bandalartCellData,
            bandalartKey = bandalartKey,
            bottomSheetDataChanged = bottomSheetDataChanged,
          )
        },
      )
    },
  ) { measurables, constraints ->
    val sub1 = measurables.first { it.layoutId == context.getString(R.string.homescreen_sub1_id_text) }
    val sub2 = measurables.first { it.layoutId == context.getString(R.string.homescreen_sub2_id_text) }
    val sub3 = measurables.first { it.layoutId == context.getString(R.string.homescreen_sub3_id_text) }
    val sub4 = measurables.first { it.layoutId == context.getString(R.string.homescreen_sub4_id_text) }
    val main = measurables.first { it.layoutId == context.getString(R.string.homescreen_main_id_text) }

    val chartWidth = paddedMaxWidth.roundToPx()
    val mainWidth = chartWidth / 5
    val padding = 1.dp.roundToPx()

    val mainConstraints = Constraints.fixed(width = mainWidth, height = mainWidth)
    val sub1Constraints = Constraints.fixed(width = mainWidth * 3 - padding, height = mainWidth * 2 - padding)
    val sub2Constraints = Constraints.fixed(width = mainWidth * 2 - padding, height = mainWidth * 3 - padding)
    val sub3Constraints = Constraints.fixed(width = mainWidth * 2 - padding, height = mainWidth * 3 - padding)
    val sub4Constraints = Constraints.fixed(width = mainWidth * 3 - padding, height = mainWidth * 2 - padding)

    val mainPlaceable = main.measure(mainConstraints)
    val sub1Placeable = sub1.measure(sub1Constraints)
    val sub2Placeable = sub2.measure(sub2Constraints)
    val sub3Placeable = sub3.measure(sub3Constraints)
    val sub4Placeable = sub4.measure(sub4Constraints)

    layout(width = chartWidth, height = chartWidth) {
      mainPlaceable.place(x = mainWidth * 2, y = mainWidth * 2)
      sub1Placeable.place(x = 0, y = 0)
      sub2Placeable.place(x = mainWidth * 3 + padding, y = 0)
      sub3Placeable.place(x = 0, y = mainWidth * 2 + padding)
      sub4Placeable.place(x = mainWidth * 2 + padding, y = mainWidth * 3 + padding)
    }
  }
}
