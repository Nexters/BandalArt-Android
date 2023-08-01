@file:OptIn(ExperimentalMaterial3Api::class)

package com.nexters.bandalart.android.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.bandalart.android.core.designsystem.R
import com.nexters.bandalart.android.core.ui.component.BandalartDropDownMenu
import com.nexters.bandalart.android.core.ui.component.CellText
import com.nexters.bandalart.android.core.ui.component.EmojiText
import com.nexters.bandalart.android.core.ui.component.FixedSizeText
import com.nexters.bandalart.android.core.ui.component.LoadingWheel
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.theme.Gray100
import com.nexters.bandalart.android.core.ui.theme.Gray200
import com.nexters.bandalart.android.core.ui.theme.Gray400
import com.nexters.bandalart.android.core.ui.theme.Gray50
import com.nexters.bandalart.android.core.ui.theme.Gray500
import com.nexters.bandalart.android.core.ui.theme.Gray600
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.Primary
import com.nexters.bandalart.android.core.ui.theme.Secondary
import com.nexters.bandalart.android.core.ui.theme.White
import com.nexters.bandalart.android.core.ui.theme.pretendard
import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.android.feature.home.ui.BottomSheetContent
import kotlinx.coroutines.launch

@Composable
internal fun HomeRoute(
  navigateToOnBoarding: () -> Unit,
  navigateToComplete: () -> Unit,
  onAddBandalart: () -> Unit,
  onShowBandalartList: () -> Unit,
  onShowSnackbar: suspend (String) -> Boolean,
  modifier: Modifier = Modifier,
  viewModel: HomeViewModel = hiltViewModel(),
) {
  val homeState by viewModel.homeUiState.collectAsStateWithLifecycle()
  HomeScreen(
    homeState = homeState,
    navigateToOnBoarding = navigateToOnBoarding,
    navigateToComplete = navigateToComplete,
    onAddBandalart = onAddBandalart,
    onShowBandalartList = onShowBandalartList,
    onShowSnackbar = onShowSnackbar,
    getBandalartMainCell = viewModel::getBandalartMainCell,
    modifier = modifier,
  )
}

@Suppress("unused")
@Composable
internal fun HomeScreen(
  homeState: HomeUiState,
  navigateToOnBoarding: () -> Unit,
  navigateToComplete: () -> Unit,
  onAddBandalart: () -> Unit,
  onShowBandalartList: () -> Unit,
  onShowSnackbar: suspend (String) -> Boolean,
  getBandalartMainCell: suspend (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val scrollState = rememberScrollState()
  val scope = rememberCoroutineScope()

  LaunchedEffect(key1 = Unit) {
    getBandalartMainCell("K3mLJ")
    // getBandalartMainCell("3sF4I")
  }
  Surface(
    modifier = modifier.fillMaxSize(),
    color = Gray50,
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .scrollable(scrollState, Orientation.Vertical),
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(bottom = 32.dp),
      ) {
        HomeTopBar(
          // 임시로 네비게이션 버튼의 역할을 대신 수행함
          // onAddBandalart = onAddBandalart,
          onAddBandalart = navigateToComplete,
          onShowBandalartList = onShowBandalartList,
        )
        Divider(
          color = Gray100,
          thickness = 1.dp,
        )
        Column(
          modifier.padding(horizontal = 16.dp),
        ) {
          Spacer(modifier = Modifier.height(24.dp))
          Box(
            modifier = Modifier.align(Alignment.CenterHorizontally),
          ) {
            Card(
              shape = RoundedCornerShape(16.dp),
              elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
              // 임시로 스낵바 호출 버튼의 역할을 대신 수행함
              modifier = Modifier.clickable(onClick = { scope.launch { onShowSnackbar("반다라트가 삭제되었어요.") } }),
            ) {
              Box(
                modifier = Modifier
                  .width(52.dp)
                  .aspectRatio(1f)
                  .background(Gray100),
                contentAlignment = Alignment.Center,
              ) {
                // TODO 데이터 연동
                EmojiText(
                  emojiText = "😎",
                  fontSize = 22.sp.nonScaleSp,
                )
              }
            }
            // TODO 데이터 연동
            // 메인 목표의 이모지가 존재하면 hide 처리 해야 함
            val image = painterResource(id = R.drawable.ic_edit)
            Image(
              painter = image,
              contentDescription = "Edit Icon",
              modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset((4).dp, (4).dp),
            )
          }
          Spacer(modifier = Modifier.height(12.dp))
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .wrapContentHeight(),
          ) {
            FixedSizeText(
              text = "완벽한 2024년",
              color = Gray900,
              fontWeight = FontWeight.W700,
              fontSize = 20.sp,
              letterSpacing = (-0.4).sp,
              modifier = Modifier.align(Alignment.Center),
            )
            var isDropDownMenuExpanded by remember { mutableStateOf(false) }
            val image = painterResource(id = R.drawable.ic_option)
            Image(
              painter = image,
              contentDescription = "Option Icon",
              modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable(onClick = { isDropDownMenuExpanded = true }),
            )
            BandalartDropDownMenu(
              onResult = { isDropDownMenuExpanded = it },
              isDropDownMenuExpanded = isDropDownMenuExpanded,
              onDeleteClicked = { },
            )
          }
          Spacer(modifier = Modifier.height(24.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            // TODO 데이터 연동
            FixedSizeText(
              text = "달성률 (0%)",
              color = Gray600,
              fontWeight = FontWeight.W500,
              fontSize = 12.sp,
              letterSpacing = (-0.24).sp,
            )
            val image =
              painterResource(id = R.drawable.ic_vertical_line)
            Image(
              painter = image,
              contentDescription = "Vertical Line Icon",
              modifier = Modifier.padding(start = 6.dp),
            )
            // TODO 데이터 연동
            FixedSizeText(
              text = "~24년 12월 31일",
              color = Gray600,
              fontWeight = FontWeight.W500,
              fontSize = 12.sp,
              letterSpacing = (-0.24).sp,
              modifier = Modifier.padding(start = 6.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
              modifier
                .clip(RoundedCornerShape(24.dp))
                .background(color = Primary),
            ) {
              Row(
                modifier = Modifier.padding(start = 9.dp, end = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = "Check Icon",
                  tint = Gray900,
                  modifier = Modifier.size(13.dp),
                )
                FixedSizeText(
                  text = "달성 완료!",
                  color = Gray900,
                  fontWeight = FontWeight.W600,
                  fontSize = 10.sp,
                  letterSpacing = (-0.2).sp,
                  modifier = Modifier.padding(start = 2.dp),
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(8.dp))
          LinearProgressBar()
          Spacer(modifier = Modifier.height(18.dp))
        }
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
        Spacer(modifier = Modifier.weight(1f))
        Box(
          modifier = Modifier
            .fillMaxWidth(),
          contentAlignment = Alignment.Center,
        ) {
          Button(
            // 임시로 네비게이션 버튼의 역할을 대신 수행함
            onClick = { navigateToOnBoarding() },
            colors = ButtonDefaults.buttonColors(
              containerColor = Gray100,
              contentColor = Gray100,
            ),
            modifier = Modifier
              .wrapContentSize()
              .clip(RoundedCornerShape(18.dp)),
          ) {
            Row {
              val image = painterResource(id = R.drawable.ic_share)
              Image(
                painter = image,
                contentDescription = "Share Icon",
              )
//              // FixedSizeText 로 적용하면 텍스트가 보이지 않음
//              FixedSizeText(
//                text = "공유하기",
//                color = Gray900,
//                fontWeight = FontWeight.W700,
//                fontSize = 12.sp,
//                modifier = Modifier.padding(start = 4.dp),
//              )
              Text(
                text = "공유하기",
                color = Gray900,
                fontFamily = pretendard,
                fontWeight = FontWeight.W700,
                fontSize = 12.sp.nonScaleSp,
                modifier = Modifier.padding(start = 4.dp),
              )
            }
          }
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
  val bandalartData: BandalartCellUiModel,
)

// TODO 서브 목표에 속한 모든 테스크의 목표를 달성할 경우 서브 목표 칸의 색상도 변경해야 함
@Composable
private fun BandalartChart(
  modifier: Modifier = Modifier,
  bandalart: BandalartCellUiModel,
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
            .background(color = Gray100),
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
          Cell(
            isMainCell = true,
            cell = bandalart,
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
          Cell(
            isMainCell = false,
            cellInfo = CellInfo(
              isSubCell = isSubCell,
              colIndex = colIndex,
              rowIndex = rowIndex,
              colCnt = cols,
              rowCnt = rows,
            ),
            modifier = Modifier.weight(1f),
            cell = if (isSubCell) subCell.bandalartData else subCell.bandalartData.children[taskIndex++],
          )
        }
      }
    }
  }
}

data class CellInfo(
  val isSubCell: Boolean = false,
  val colIndex: Int = 2,
  val rowIndex: Int = 2,
  val colCnt: Int = 1,
  val rowCnt: Int = 1,
)

@Composable
fun Cell(
  modifier: Modifier = Modifier,
  isMainCell: Boolean,
  cellInfo: CellInfo = CellInfo(),
  cell: BandalartCellUiModel,
  outerPadding: Dp = 3.dp,
  innerPadding: Dp = 2.dp,
  mainCellPadding: Dp = 1.dp,
) {
  var openBottomSheet by rememberSaveable { mutableStateOf(false) }
  val skipPartiallyExpanded by remember { mutableStateOf(true) }
  val scope = rememberCoroutineScope()
  val bottomSheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = skipPartiallyExpanded,
  )
  val backgroundColor = when {
    isSubCell -> Secondary
    cell.isCompleted -> Gray200
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
      .background(if (isMainCell) Primary else if (cellInfo.isSubCell) Secondary else White)
      .clickable { openBottomSheet = !openBottomSheet },
    contentAlignment = Alignment.Center,
  ) {
    if (isMainCell) {
      if (cell.title.isNullOrEmpty()) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Add Icon",
          tint = Secondary,
          modifier = Modifier.size(20.dp),
        )
      }
      CellText(
        cellText = cell.title,
        cellColor = Secondary,
        fontWeight = FontWeight.W700,
      )
    } else if (cellInfo.isSubCell) {
      val cellTextColor = Primary
      val fontWeight = FontWeight.W700
      if (cell.title.isNullOrEmpty()) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center,
        ) {
          CellText(
            cellText = "서브목표",
            cellTextColor = cellTextColor,
            fontWeight = fontWeight,
          )
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Icon",
            tint = cellTextColor,
            modifier = Modifier.size(20.dp),
          )
        }
      } else {
        CellText(
          cellText = cell.title,
          cellTextColor = cellTextColor,
          fontWeight = fontWeight,
        )
      }
    } else {
      // 테스크
      val cellTextColor = if (cell.isCompleted) Gray400 else Gray900
      val fontWeight = FontWeight.W500

      // 테스크가 비어있는 경우
      if (cell.title.isNullOrEmpty()) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Add Icon",
          tint = Gray500,
          modifier = Modifier.size(20.dp),
        )
      } else {
        // 테스크 목푤르 달성한 경우
        if (cell.isCompleted) {
          Box(
            modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
          ) {
            CellText(
              cellText = cell.title,
              cellTextColor = cellTextColor,
              fontWeight = fontWeight,
            )
            val image = painterResource(id = R.drawable.ic_cell_check)
            Image(
              painter = image,
              contentDescription = "Complete Icon",
              modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-4).dp, y = (-4).dp),
            )
          }
        } else {
          CellText(
            cellText = cell.title,
            cellTextColor = cellTextColor,
            fontWeight = fontWeight,
          )
        }
      }
    }
    if (openBottomSheet) {
      ModalBottomSheet(
        modifier = Modifier
          .wrapContentSize()
          .statusBarsPadding(),
        onDismissRequest = { openBottomSheet = false },
        sheetState = bottomSheetState,
        content = BottomSheetContent(
          onResult = { openBottomSheet = it },
          scope = scope,
          bottomSheetState = bottomSheetState,
          isSubCell = cellInfo.isSubCell,
          isMainCell = isMainCell,
        ),
        dragHandle = null,
      )
    }
  }
}
