package com.nexters.bandalart.android.feature.home

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.bandalart.android.core.ui.CellText
import com.nexters.bandalart.android.core.ui.LoadingWheel
import com.nexters.bandalart.android.core.ui.NavButton
import com.nexters.bandalart.android.core.ui.theme.Gray200
import com.nexters.bandalart.android.core.ui.theme.Primary
import com.nexters.bandalart.android.core.ui.theme.Secondary
import com.nexters.bandalart.android.feature.home.model.BandalartMainCellUiModel
import kotlinx.coroutines.launch

@Composable
internal fun HomeRoute(
  navigateToOnBoarding: () -> Unit,
  navigateToComplete: () -> Unit,
  onShowSnackbar: suspend (String) -> Boolean,
  modifier: Modifier = Modifier,
  viewModel: HomeViewModel = hiltViewModel(),
) {
  val homeState by viewModel.homeUiState.collectAsStateWithLifecycle()
  HomeScreen(
    homeState = homeState,
    navigateToOnBoarding = navigateToOnBoarding,
    navigateToComplete = navigateToComplete,
    onShowSnackbar = onShowSnackbar,
    getBandalartMainCell = viewModel::getBandalartMainCell,
    modifier = modifier,
  )
}

@Composable
internal fun HomeScreen(
  homeState: HomeUiState,
  navigateToOnBoarding: () -> Unit,
  navigateToComplete: () -> Unit,
  onShowSnackbar: suspend (String) -> Boolean,
  getBandalartMainCell: suspend (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val scope = rememberCoroutineScope()

  LaunchedEffect(key1 = Unit) {
    getBandalartMainCell("3sF4I")
  }
  Surface(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
    ) {
      Text(
        text = "Home Screen",
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
      )
      Spacer(modifier = Modifier.height(16.dp))
      Column(
        modifier = Modifier.wrapContentWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        NavButton(
          onClick = navigateToOnBoarding,
          route = "OnBoarding",
        )
        NavButton(
          onClick = navigateToComplete,
          route = "Complete",
        )
        Button(
          onClick = {
            scope.launch {
              onShowSnackbar("메세지 출력")
            }
          },
        ) {
          Text(
            text = "스낵바 띄우기",
          )
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
      when (homeState) {
        is HomeUiState.Loading -> {
          LoadingWheel()
        }
        is HomeUiState.Success -> {
          BandalartChart(bandalart = homeState.bandalartData)
        }
        is HomeUiState.Error -> {
          // TODO ErrorScreen()
        }
      }
    }
  }
}

data class SubCell(
  val rowCnt: Int,
  val colCnt: Int,
  val subCellRowIndex: Int,
  val subCellColIndex: Int,
  val bandalartData: BandalartMainCellUiModel,
)

@Composable
private fun BandalartChart(
  modifier: Modifier = Modifier,
  bandalart: BandalartMainCellUiModel,
) {
  val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
  val paddedMaxWidth = remember(screenWidthDp) {
    screenWidthDp - (15.dp * 2)
  }

  val subCellList = listOf(
    SubCell(2, 3, 1, 1, bandalart.children[0]),
    SubCell(3, 2, 1, 0, bandalart.children[1]),
    SubCell(3, 2, 1, 1, bandalart.children[2]),
    SubCell(2, 3, 0, 1, bandalart.children[3]),
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
            .background(color = Gray200),
          content = {
            CellGrid(
              rows = subCellList[index].rowCnt,
              cols = subCellList[index].colCnt,
              subCell = subCellList[index],
            )
          },
        )
      }
      Box(
        modifier
          .layoutId("Main")
          .clip(RoundedCornerShape(10.dp))
          .background(color = Primary),
        content = {
          Box(
            modifier = Modifier
              .matchParentSize()
              .padding(1.dp),
            contentAlignment = Alignment.Center,
          ) {
            CellText(
              cellText = bandalart.title,
              cellColor = Secondary,
              fontWeight = FontWeight.W700,
            )
          }
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

    val mainConstraints =
      Constraints.fixed(width = mainWidth, height = mainWidth)
    val sub1Constraints =
      Constraints.fixed(width = mainWidth * 3 - padding, height = mainWidth * 2 - padding)
    val sub2Constraints =
      Constraints.fixed(width = mainWidth * 2 - padding, height = mainWidth * 3 - padding)
    val sub3Constraints =
      Constraints.fixed(width = mainWidth * 2 - padding, height = mainWidth * 3 - padding)
    val sub4Constraints =
      Constraints.fixed(width = mainWidth * 3 - padding, height = mainWidth * 2 - padding)

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
fun CellGrid(
  rows: Int,
  cols: Int,
  subCell: SubCell,
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceEvenly,
  ) {
    var taskIndex = 0
    repeat(rows) { rowIndex ->
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
      ) {
        repeat(cols) { colIndex ->
          val isSubCell = rowIndex == subCell.subCellRowIndex && colIndex == subCell.subCellColIndex
          val cellText =
            if (isSubCell) subCell.bandalartData.title
            else subCell.bandalartData.children[taskIndex++].title
          Cell(
            cellText = cellText,
            isSubCell = isSubCell,
            colIndex = colIndex,
            rowIndex = rowIndex,
            colCnt = cols,
            rowCnt = rows,
            modifier = Modifier.weight(1f),
          )
        }
      }
    }
  }
}

@Composable
fun Cell(
  modifier: Modifier = Modifier,
  cellText: String,
  isSubCell: Boolean,
  colIndex: Int,
  rowIndex: Int,
  colCnt: Int,
  rowCnt: Int,
  outerPadding: Dp = 3.dp,
  innerPadding: Dp = 2.dp,
) {
  Box(
    modifier = modifier
      .padding(
        start = if (colIndex == 0) outerPadding else innerPadding,
        end = if (colIndex == colCnt - 1) outerPadding else innerPadding,
        top = if (rowIndex == 0) outerPadding else innerPadding,
        bottom = if (rowIndex == rowCnt - 1) outerPadding else innerPadding,
      )
      .aspectRatio(1f)
      .background(Gray200)
      .clip(RoundedCornerShape(10.dp))
      .background(if (isSubCell) Secondary else Color.White),
    contentAlignment = Alignment.Center,
  ) {
    if (isSubCell) {
      CellText(
        cellText = cellText,
        cellColor = Primary,
        fontWeight = FontWeight.W700,
      )
    } else {
      CellText(
        cellText = cellText,
        cellColor = Secondary,
        fontWeight = FontWeight.W500,
      )
    }
  }
}
