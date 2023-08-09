package com.nexters.bandalart.android.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.R
import com.nexters.bandalart.android.core.ui.component.FixedSizeText
import com.nexters.bandalart.android.core.ui.theme.Gray100
import com.nexters.bandalart.android.core.ui.theme.Gray300
import com.nexters.bandalart.android.core.ui.theme.Gray50
import com.nexters.bandalart.android.core.ui.theme.Gray600
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.White

@Composable
fun BandalartSkeletonScreen(
  modifier: Modifier = Modifier,
  brush: Brush,
) {
  Surface(
    modifier = modifier.fillMaxSize(),
    color = Gray50,
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(bottom = 32.dp),
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .background(White),
          contentAlignment = Alignment.CenterStart,
        ) {
          Row(modifier = Modifier.fillMaxWidth()) {
            val image = painterResource(id = R.drawable.ic_app)
            Image(
              painter = image,
              contentDescription = "App Icon",
              modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 20.dp),
            )
          }
        }
        HorizontalDivider(
          thickness = 1.dp,
          color = Gray100,
        )
        Column(modifier.padding(horizontal = 16.dp)) {
          Spacer(modifier = Modifier.height(24.dp))
          Box(modifier = Modifier.wrapContentHeight()) {
            Column {
              Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Card(
                  shape = RoundedCornerShape(16.dp),
                  elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                ) {
                  Box(
                    modifier = Modifier
                      .width(52.dp)
                      .aspectRatio(1f)
                      .background(Gray100),
                    contentAlignment = Alignment.Center,
                  ) {
                    val image = painterResource(id = R.drawable.ic_empty_emoji)
                    Image(
                      painter = image,
                      contentDescription = "Empty Emoji Icon",
                    )
                  }
                }
                val image = painterResource(id = R.drawable.ic_edit)
                Image(
                  painter = image,
                  contentDescription = "Edit Icon",
                  modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = 4.dp),
                )
              }
              Spacer(modifier = Modifier.height(12.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .wrapContentHeight(),
              ) {
                FixedSizeText(
                  text = "                      ",
                  color = Gray900,
                  fontWeight = FontWeight.W700,
                  fontSize = 20.sp,
                  letterSpacing = (-0.4).sp,
                  modifier = Modifier
                    .align(Alignment.Center)
                    .background(brush),
                )
                val image = painterResource(id = R.drawable.ic_option)
                Image(
                  painter = image,
                  contentDescription = "Option Icon",
                  modifier = Modifier.align(Alignment.CenterEnd),
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(24.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            FixedSizeText(
              text = "달성률 (--%)",
              color = Gray600,
              fontWeight = FontWeight.W500,
              fontSize = 12.sp,
              letterSpacing = (-0.24).sp,
            )
            Spacer(modifier = Modifier.weight(1f))
          }
        }
        Spacer(modifier = Modifier.height(8.dp))
        CompletionRatioProgressBar(
          completionRatio = 70,
          progressColor = Gray300,
        )
        Spacer(modifier = Modifier.height(18.dp))
        BandalartSkeletonChart(brush = brush)
      }
    }
  }
}

data class SubCell(
  val rowCnt: Int,
  val colCnt: Int,
  val subCellRowIndex: Int,
  val subCellColIndex: Int,
)

@Composable
fun BandalartSkeletonChart(
  modifier: Modifier = Modifier,
  brush: Brush,
) {
  val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
  val paddedMaxWidth = remember(screenWidthDp) {
    screenWidthDp - (15.dp * 2)
  }

  val subCellList = listOf(
    SubCell(2, 3, 1, 1),
    SubCell(3, 2, 1, 0),
    SubCell(3, 2, 1, 1),
    SubCell(2, 3, 0, 1),
  )

  Layout(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp)),
    content = {
      for (index in subCellList.indices) {
        Box(
          modifier
            .layoutId("Sub ${index + 1}")
            .clip(RoundedCornerShape(12.dp))
            .background(color = Gray100),
          content = {
            SkeletonCellGrid(
              rows = subCellList[index].rowCnt,
              cols = subCellList[index].colCnt,
              subCell = subCellList[index],
              brush = brush,
            )
          },
        )
      }
      Box(
        modifier
          .layoutId("Main")
          .clip(RoundedCornerShape(10.dp))
          .background(brush = brush),
        content = {
          SkeletonCell(
            isMainCell = true,
            brush = brush,
          )
        },
      )
    },
  ) { measurables, constraints ->
    val sub1 = measurables.first { it.layoutId == "Sub 1" }
    val sub2 = measurables.first { it.layoutId == "Sub 2" }
    val sub3 = measurables.first { it.layoutId == "Sub 3" }
    val sub4 = measurables.first { it.layoutId == "Sub 4" }
    val main = measurables.first { it.layoutId == "Main" }

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

@Composable
fun SkeletonCellGrid(
  rows: Int,
  cols: Int,
  subCell: SubCell,
  brush: Brush,
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceEvenly,
  ) {
    repeat(rows) { rowIndex ->
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
      ) {
        repeat(cols) { colIndex ->
          val isSubCell = rowIndex == subCell.subCellRowIndex && colIndex == subCell.subCellColIndex
          SkeletonCell(
            isMainCell = false,
            skeletonCellInfo = SkeletonCellInfo(
              isSubCell = isSubCell,
              colIndex = colIndex,
              rowIndex = rowIndex,
              colCnt = cols,
              rowCnt = rows,
            ),
            modifier = Modifier.weight(1f),
            brush = brush,
          )
        }
      }
    }
  }
}

data class SkeletonCellInfo(
  val isSubCell: Boolean = false,
  val colIndex: Int = 2,
  val rowIndex: Int = 2,
  val colCnt: Int = 1,
  val rowCnt: Int = 1,
)

@Composable
fun SkeletonCell(
  modifier: Modifier = Modifier,
  isMainCell: Boolean,
  brush: Brush,
  skeletonCellInfo: SkeletonCellInfo = SkeletonCellInfo(),
  outerPadding: Dp = 3.dp,
  innerPadding: Dp = 2.dp,
  mainCellPadding: Dp = 1.dp,
) {
  Box(
    modifier = modifier
      .padding(
        start = if (isMainCell) mainCellPadding else if (skeletonCellInfo.colIndex == 0) outerPadding else innerPadding,
        end = if (isMainCell) mainCellPadding else if (skeletonCellInfo.colIndex == skeletonCellInfo.colCnt - 1) outerPadding else innerPadding,
        top = if (isMainCell) mainCellPadding else if (skeletonCellInfo.rowIndex == 0) outerPadding else innerPadding,
        bottom = if (isMainCell) mainCellPadding else if (skeletonCellInfo.rowIndex == skeletonCellInfo.rowCnt - 1) outerPadding else innerPadding,
      )
      .aspectRatio(1f)
      .clip(RoundedCornerShape(10.dp))
      .background(brush),
    contentAlignment = Alignment.Center,
  ) { }
}
