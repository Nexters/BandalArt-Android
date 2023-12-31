package com.nexters.bandalart.android.feature.home.ui.bandalart

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.android.core.designsystem.theme.Gray100
import com.nexters.bandalart.android.core.designsystem.theme.MainColor
import com.nexters.bandalart.android.core.ui.ComponentPreview
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.ThemeColor
import com.nexters.bandalart.android.core.ui.allColor
import com.nexters.bandalart.android.core.ui.extension.toColor
import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.android.feature.home.model.dummyBandalartChartData

@Composable
fun BandalartChart(
  bandalartKey: String,
  bandalartCellData: BandalartCellUiModel,
  themeColor: ThemeColor,
  bottomSheetDataChanged: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
  val paddedMaxWidth = remember(screenWidthDp) {
    screenWidthDp - (15.dp * 2)
  }

  val subCellList = listOf(
    SubCell(2, 3, 1, 1, bandalartCellData.children[0]),
    SubCell(3, 2, 1, 0, bandalartCellData.children[1]),
    SubCell(3, 2, 1, 1, bandalartCellData.children[2]),
    SubCell(2, 3, 0, 1, bandalartCellData.children[3]),
  )

  Layout(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp)),
    content = {
      for (index in subCellList.indices) {
        Box(
          modifier
            .layoutId(stringResource(R.string.home_layout_id, index + 1))
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
          .layoutId(stringResource(R.string.home_main_id))
          .clip(RoundedCornerShape(10.dp))
          .background(color = (bandalartCellData.mainColor?.toColor() ?: MainColor)),
        content = {
          BandalartCell(
            isMainCell = true,
            themeColor = themeColor,
            cellData = bandalartCellData,
            bandalartKey = bandalartKey,
            bottomSheetDataChanged = bottomSheetDataChanged,
          )
        },
      )
    },
  ) { measurables, constraints ->
    val sub1 = measurables.first { it.layoutId == context.getString(R.string.home_sub1_id) }
    val sub2 = measurables.first { it.layoutId == context.getString(R.string.home_sub2_id) }
    val sub3 = measurables.first { it.layoutId == context.getString(R.string.home_sub3_id) }
    val sub4 = measurables.first { it.layoutId == context.getString(R.string.home_sub4_id) }
    val main = measurables.first { it.layoutId == context.getString(R.string.home_main_id) }

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

@ComponentPreview
@Composable
fun BandalartChartPreview() {
  BandalartChart(
    bandalartKey = "",
    bandalartCellData = dummyBandalartChartData,
    themeColor = allColor[0],
    bottomSheetDataChanged = {},
  )
}
