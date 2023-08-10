@file:OptIn(ExperimentalMaterial3Api::class)

package com.nexters.bandalart.android.feature.home

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.bandalart.android.core.designsystem.R
import com.nexters.bandalart.android.core.ui.component.BandalartDeleteAlertDialog
import com.nexters.bandalart.android.core.ui.component.BandalartDropDownMenu
import com.nexters.bandalart.android.core.ui.component.CellText
import com.nexters.bandalart.android.core.ui.component.EmojiText
import com.nexters.bandalart.android.core.ui.component.FixedSizeText
import com.nexters.bandalart.android.core.ui.component.LoadingScreen
import com.nexters.bandalart.android.core.ui.component.NetworkErrorAlertDialog
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.extension.toColor
import com.nexters.bandalart.android.core.ui.extension.toFormatDate
import com.nexters.bandalart.android.core.ui.theme.Gray100
import com.nexters.bandalart.android.core.ui.theme.Gray200
import com.nexters.bandalart.android.core.ui.theme.Gray300
import com.nexters.bandalart.android.core.ui.theme.Gray400
import com.nexters.bandalart.android.core.ui.theme.Gray50
import com.nexters.bandalart.android.core.ui.theme.Gray500
import com.nexters.bandalart.android.core.ui.theme.Gray600
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.White
import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.android.feature.home.model.BandalartDetailUiModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartMainCellModel
import com.nexters.bandalart.android.feature.home.ui.BandalartBottomSheet
import com.nexters.bandalart.android.feature.home.ui.BandalartEmojiPicker
import com.nexters.bandalart.android.feature.home.ui.BandalartListBottomSheet
import com.nexters.bandalart.android.feature.home.ui.BandalartSkeleton
import com.nexters.bandalart.android.feature.home.ui.CompletionRatioProgressBar
import com.nexters.bandalart.android.feature.home.ui.HomeTopBar
import com.nexters.bandalart.android.feature.home.ui.ThemeColor

@Composable
internal fun HomeRoute(
  modifier: Modifier = Modifier,
  navigateToComplete: () -> Unit,
  onShowSnackbar: suspend (String) -> Boolean,
  viewModel: HomeViewModel = hiltViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(viewModel) {
    viewModel.eventFlow.collect { event ->
      when (event) {
        is HomeUiEvent.ShowSnackbar -> {
          onShowSnackbar(event.message)
        }
      }
    }
  }

  HomeScreen(
    modifier = modifier,
    uiState = uiState,
    bandalartList = uiState.bandalartList,
    bandalartDetailData = uiState.bandalartDetailData ?: BandalartDetailUiModel(),
    navigateToComplete = navigateToComplete,
    updateBandalartMainCell = viewModel::updateBandalartMainCell,
    getBandalartList = { key: String? -> viewModel.getBandalartList(key) },
    getBandalartDetail = viewModel::getBandalartDetail,
    createBandalart = viewModel::createBandalart,
    deleteBandalart = viewModel::deleteBandalart,
    openDropDownMenu = { state -> viewModel.openDropDownMenu(state) },
    openBandalartDeleteAlertDialog = { state -> viewModel.openBandalartDeleteAlertDialog(state) },
    bottomSheetDataChanged = { state -> viewModel.bottomSheetDataChanged(state) },
    openNetworkErrorGetBandalartAlertDialog = { state -> viewModel.openNetworkErrorGetBandalartAlertDialog(state) },
    openLimitCreateBandalartAlertDialog = { state -> viewModel.openLimitCreateBandalartAlertDialog(state) },
    openNetworkErrorCreateBandalartAlertDialog = { state -> viewModel.openNetworkErrorCreateBandalartAlertDialog(state) },
    openNetworkErrorDeleteBandalartAlertDialog = { state -> viewModel.openNetworkErrorDeleteBandalartAlertDialog(state) },
    openNetworkErrorUpdateBandalartAlertDialog = { state -> viewModel.openNetworkErrorUpdateBandalartAlertDialog(state) },
    openNetworkErrorShareBandalartAlertDialog = { state -> viewModel.openNetworkErrorShareBandalartAlertDialog(state) },
    openBandalartListBottomSheet = { state -> viewModel.openBandalartListBottomSheet(state) },
    loadingChanged = { state -> viewModel.loadingChanged(state) },
    showSkeletonChanged = { state -> viewModel.showSkeletonChanged(state) },
    setRecentBandalartKey = { key -> viewModel.setRecentBandalartKey(key) },
    shareBandalart = { key: String -> viewModel.shareBandalart(key) },
    initShareUrl = viewModel::initShareUrl,
  )
}

@Suppress("unused")
@Composable
internal fun HomeScreen(
  modifier: Modifier = Modifier,
  uiState: HomeUiState,
  bandalartList: List<BandalartDetailUiModel>,
  bandalartDetailData: BandalartDetailUiModel,
  navigateToComplete: () -> Unit,
  updateBandalartMainCell: (String, String, UpdateBandalartMainCellModel) -> Unit,
  getBandalartList: (String?) -> Unit,
  getBandalartDetail: (String) -> Unit,
  createBandalart: () -> Unit,
  deleteBandalart: (String) -> Unit,
  openDropDownMenu: (Boolean) -> Unit,
  openBandalartDeleteAlertDialog: (Boolean) -> Unit,
  bottomSheetDataChanged: (Boolean) -> Unit,
  openNetworkErrorGetBandalartAlertDialog: (Boolean) -> Unit,
  openLimitCreateBandalartAlertDialog: (Boolean) -> Unit,
  openNetworkErrorCreateBandalartAlertDialog: (Boolean) -> Unit,
  openNetworkErrorDeleteBandalartAlertDialog: (Boolean) -> Unit,
  openNetworkErrorUpdateBandalartAlertDialog: (Boolean) -> Unit,
  openNetworkErrorShareBandalartAlertDialog: (Boolean) -> Unit,
  openBandalartListBottomSheet: (Boolean) -> Unit,
  loadingChanged: (Boolean) -> Unit,
  showSkeletonChanged: (Boolean) -> Unit,
  setRecentBandalartKey: (String) -> Unit,
  shareBandalart: (String) -> Unit,
  initShareUrl: () -> Unit,
) {
  val scrollState = rememberScrollState()
  var openEmojiBottomSheet by rememberSaveable { mutableStateOf(false) }
  val emojiSkipPartiallyExpanded by remember { mutableStateOf(true) }
  val emojiPickerScope = rememberCoroutineScope()
  val emojiPickerState = rememberModalBottomSheetState(
    skipPartiallyExpanded = emojiSkipPartiallyExpanded,
  )
  val context = LocalContext.current

  // TODO null 를 파라미터로 넣어줘야 하는 이유 학습
  LaunchedEffect(key1 = Unit) {
    getBandalartList(null)
  }

  LaunchedEffect(key1 = bandalartDetailData.isCompleted) {
    if (bandalartDetailData.isCompleted) {
      navigateToComplete()
    }
  }

  LaunchedEffect(key1 = uiState.isBottomSheetDataChanged) {
    if (uiState.isBottomSheetDataChanged) {
      loadingChanged(true)
      getBandalartList(null)
    }
  }

  LaunchedEffect(key1 = uiState.isBandalartDeleted) {
    if (uiState.isBandalartDeleted) {
      openBandalartDeleteAlertDialog(false)
    }
  }

  LaunchedEffect(key1 = uiState.shareUrl) {
    if (uiState.shareUrl.isNotEmpty()) {
      val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
          Intent.EXTRA_TEXT,
          "제가 세운 반다라트를 구경하러오세요! \n ${uiState.shareUrl}",
        )
        type = "text/plain"
      }
      val shareIntent = Intent.createChooser(sendIntent, null)
      context.startActivity(shareIntent)
      initShareUrl()
    }
  }

  if (uiState.isBandalartListBottomSheetOpened) {
    BandalartListBottomSheet(
      bandalartList = uiState.bandalartList,
      currentBandalartKey = bandalartDetailData.key,
      getBandalartDetail = getBandalartDetail,
      setRecentBandalartKey = setRecentBandalartKey,
      showSkeletonChanged = showSkeletonChanged,
      onCancelClicked = { openBandalartListBottomSheet(false) },
      createBandalart = createBandalart,
    )
  }

  if (uiState.isBandalartDeleteAlertDialogOpened) {
    BandalartDeleteAlertDialog(
      title = if (bandalartDetailData.title.isNullOrEmpty()) {
        "지금 작성중인\n반다라트를 삭제하시겠어요?"
      } else {
        "'${bandalartDetailData.title}'\n반다라트를 삭제하시겠어요?"
      },
      message = "삭제된 반다라트는 다시 복구할 수 없어요.",
      onDeleteClicked = { deleteBandalart(bandalartDetailData.key) },
      onCancelClicked = { openBandalartDeleteAlertDialog(false) },
    )
  }

  if (uiState.isNetworkErrorCreateBandalartAlertDialogOpened) {
    NetworkErrorAlertDialog(
      title = "네트워크 문제로 표를\n생성하지 못했어요",
      message = "다시 시도해주시기 바랍니다.",
      onConfirmClick = { openNetworkErrorCreateBandalartAlertDialog(false) },
    )
  }

  if (uiState.isLimitCreateBandalartAlertDialogOpened) {
    NetworkErrorAlertDialog(
      title = "제한된 횟수로 인해 표를\n생성하지 못했어요",
      message = "최대 생성 가능 개수는 2개입니다.",
      onConfirmClick = { openLimitCreateBandalartAlertDialog(false) },
    )
  }

  if (uiState.isNetworkErrorGetBandalartAlertDialogOpened) {
    NetworkErrorAlertDialog(
      title = "네트워크 문제로 표를\n불러오지 못했어요",
      message = "다시 시도해주시기 바랍니다.",
      onConfirmClick = { openNetworkErrorGetBandalartAlertDialog(false) },
    )
  }

  if (uiState.isNetworkErrorDeleteBandalartAlertDialogOpened) {
    NetworkErrorAlertDialog(
      title = "네트워크 문제로 표를\n삭제하지 못했어요",
      message = "다시 시도해주시기 바랍니다.",
      onConfirmClick = { openNetworkErrorDeleteBandalartAlertDialog(false) },
    )
  }

  if (uiState.isNetworkErrorUpdateBandalartAlertDialogOpened) {
    NetworkErrorAlertDialog(
      title = "네트워크 문제로 표를\n수정하지 못했어요",
      message = "다시 시도해주시기 바랍니다.",
      onConfirmClick = { openNetworkErrorUpdateBandalartAlertDialog(false) },
    )
  }

  if (uiState.isNetworkErrorShareBandalartAlertDialogOpened) {
    NetworkErrorAlertDialog(
      title = "네트워크 문제로 공유 링크를\n불러오지 못했어요",
      message = "다시 시도해주시기 바랍니다.",
      onConfirmClick = { openNetworkErrorShareBandalartAlertDialog(false) },
    )
  }
  Surface(
    modifier = modifier.fillMaxSize(),
    color = Gray50,
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(scrollState)
          .padding(bottom = 32.dp),
      ) {
        HomeTopBar(
          bandalartCount = bandalartList.size,
          onShowBandalartList = { openBandalartListBottomSheet(true) },
        )
        HorizontalDivider(
          thickness = 1.dp,
          color = Gray100,
        )
        Column(
          modifier.padding(horizontal = 16.dp),
        ) {
          Spacer(modifier = Modifier.height(24.dp))
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
                    .background(Gray100)
                    .clickable { openEmojiBottomSheet = !openEmojiBottomSheet },
                  contentAlignment = Alignment.Center,
                ) {
                  if (bandalartDetailData.profileEmoji.isNullOrEmpty()) {
                    val image = painterResource(id = R.drawable.ic_empty_emoji)
                    Image(
                      painter = image,
                      contentDescription = "Empty Emoji Icon",
                    )
                  } else {
                    EmojiText(
                      emojiText = bandalartDetailData.profileEmoji,
                      fontSize = 22.sp,
                    )
                  }
                }
                if (openEmojiBottomSheet) {
                  ModalBottomSheet(
                    modifier = Modifier.wrapContentSize(),
                    onDismissRequest = { openEmojiBottomSheet = !openEmojiBottomSheet },
                    sheetState = emojiPickerState,
                    content = BandalartEmojiPicker(
                      currentEmoji = uiState.bandalartCellData?.profileEmoji,
                      isBottomSheet = true,
                      onResult = { currentEmojiResult, openEmojiBottomSheetResult ->
                        openEmojiBottomSheet = openEmojiBottomSheetResult
                        updateBandalartMainCell(
                          bandalartDetailData.key,
                          uiState.bandalartCellData!!.key,
                          UpdateBandalartMainCellModel(
                            title = uiState.bandalartCellData.title ?: "",
                            description = uiState.bandalartCellData.description,
                            dueDate = uiState.bandalartCellData.dueDate,
                            profileEmoji = currentEmojiResult,
                            mainColor = uiState.bandalartCellData.mainColor!!,
                            subColor = uiState.bandalartCellData.subColor!!,
                          ),
                        )
                      },
                      emojiPickerScope = emojiPickerScope,
                      emojiPickerState = emojiPickerState,
                    ),
                    dragHandle = null,
                  )
                }
              }
              if (bandalartDetailData.profileEmoji.isNullOrEmpty()) {
                val image = painterResource(id = R.drawable.ic_edit)
                Image(
                  painter = image,
                  contentDescription = "Edit Icon",
                  modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = 4.dp),
                )
              }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            ) {
              var openBottomSheet by rememberSaveable { mutableStateOf(false) }
              FixedSizeText(
                text = bandalartDetailData.title ?: "메인 목표를 입력해주세요",
                color = if (bandalartDetailData.title.isNullOrEmpty()) Gray300 else Gray900,
                fontWeight = FontWeight.W700,
                fontSize = 20.sp,
                letterSpacing = (-0.4).sp,
                modifier = Modifier
                  .align(Alignment.Center)
                  .clickable { openBottomSheet = !openBottomSheet },
              )
              if (openBottomSheet) {
                BandalartBottomSheet(
                  bandalartKey = bandalartDetailData.key,
                  isSubCell = false,
                  isMainCell = true,
                  isBlankCell = uiState.bandalartCellData!!.title.isNullOrEmpty(),
                  cellData = uiState.bandalartCellData,
                  onResult = { bottomSheetState, bottomSheetDataChangedState ->
                    openBottomSheet = bottomSheetState
                    bottomSheetDataChanged(bottomSheetDataChangedState)
                  },
                )
              }
              val image = painterResource(id = R.drawable.ic_option)
              Image(
                painter = image,
                contentDescription = "Option Icon",
                modifier = Modifier
                  .align(Alignment.CenterEnd)
                  .clickable(onClick = { openDropDownMenu(true) }),
              )
              BandalartDropDownMenu(
                openDropDownMenu = openDropDownMenu,
                isDropDownMenuOpened = uiState.isDropDownMenuOpened,
                onDeleteClicked = {
                  openBandalartDeleteAlertDialog(true)
                  openDropDownMenu(false)
                },
              )
            }
          }
          Spacer(modifier = Modifier.height(24.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            FixedSizeText(
              text = "달성률 (${bandalartDetailData.completionRatio}%)",
              color = Gray600,
              fontWeight = FontWeight.W500,
              fontSize = 12.sp,
              letterSpacing = (-0.24).sp,
            )
            if (!bandalartDetailData.dueDate.isNullOrEmpty()) {
              VerticalDivider(
                modifier = Modifier
                  .height(8.dp)
                  .padding(start = 6.dp),
                thickness = 1.dp,
                color = Gray300,
              )
              FixedSizeText(
                text = bandalartDetailData.dueDate.toFormatDate(),
                color = Gray600,
                fontWeight = FontWeight.W500,
                fontSize = 12.sp,
                letterSpacing = (-0.24).sp,
                modifier = Modifier.padding(start = 6.dp),
              )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (uiState.bandalartDetailData != null && uiState.bandalartDetailData.isCompleted) {
              Box(
                modifier
                  .clip(RoundedCornerShape(24.dp))
                  .background(color = bandalartDetailData.mainColor.toColor()),
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 9.dp),
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
          }
          Spacer(modifier = Modifier.height(8.dp))
          CompletionRatioProgressBar(
            completionRatio = uiState.bandalartCellData?.completionRatio ?: 0,
            progressColor = bandalartDetailData.mainColor.toColor(),
          )
          Spacer(modifier = Modifier.height(18.dp))
        }
        when {
          uiState.bandalartCellData != null -> {
            BandalartChart(
              bandalartChartData = uiState.bandalartCellData,
              themeColor = ThemeColor(
                mainColor = bandalartDetailData.mainColor,
                subColor = bandalartDetailData.subColor,
              ),
              bottomSheetDataChanged = bottomSheetDataChanged,
              bandalartKey = bandalartDetailData.key,
            )
          }
        }
        Spacer(modifier = Modifier.height(64.dp))
        Spacer(modifier = Modifier.weight(1f))
        Box(
          modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(18.dp))
            .background(Gray100)
            .clickable { shareBandalart(bandalartDetailData.key) }
            .align(Alignment.CenterHorizontally),
          contentAlignment = Alignment.Center,
        ) {
          Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 20.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            val image = painterResource(id = R.drawable.ic_share)
            Image(
              painter = image,
              contentDescription = "Share Icon",
            )
            FixedSizeText(
              text = "공유하기",
              modifier = Modifier.padding(start = 4.dp),
              color = Gray900,
              fontSize = 12.sp.nonScaleSp,
              fontWeight = FontWeight.W700,
            )
          }
        }
      }
      when {
        uiState.isLoading -> {
          LoadingScreen()
        }
        uiState.isShowSkeleton -> {
          BandalartSkeleton()
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
  val bandalartChartData: BandalartCellUiModel,
)

@Composable
private fun BandalartChart(
  modifier: Modifier = Modifier,
  bandalartChartData: BandalartCellUiModel,
  themeColor: ThemeColor,
  bandalartKey: String,
  bottomSheetDataChanged: (Boolean) -> Unit,
) {
  val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
  val paddedMaxWidth = remember(screenWidthDp) {
    screenWidthDp - (15.dp * 2)
  }

  val subCellList = listOf(
    SubCell(2, 3, 1, 1, bandalartChartData.children[0]),
    SubCell(3, 2, 1, 0, bandalartChartData.children[1]),
    SubCell(3, 2, 1, 1, bandalartChartData.children[2]),
    SubCell(2, 3, 0, 1, bandalartChartData.children[3]),
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
              themeColor = themeColor,
              bandalartKey = bandalartKey,
              bottomSheetDataChanged = bottomSheetDataChanged,
            )
          },
        )
      }
      Box(
        modifier
          .layoutId("Main")
          .clip(RoundedCornerShape(10.dp))
          .background(color = themeColor.mainColor.toColor()),
        content = {
          Cell(
            isMainCell = true,
            themeColor = themeColor,
            cellData = bandalartChartData,
            bandalartKey = bandalartKey,
            bottomSheetDataChanged = bottomSheetDataChanged,
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
fun CellGrid(
  rows: Int,
  cols: Int,
  subCell: SubCell,
  themeColor: ThemeColor,
  bandalartKey: String,
  bottomSheetDataChanged: (Boolean) -> Unit,
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
            cellData = if (isSubCell) subCell.bandalartChartData else subCell.bandalartChartData.children[taskIndex++],
            themeColor = themeColor,
            bandalartKey = bandalartKey,
            bottomSheetDataChanged = bottomSheetDataChanged,
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
  cellData: BandalartCellUiModel,
  bandalartKey: String,
  bottomSheetDataChanged: (Boolean) -> Unit,
  outerPadding: Dp = 3.dp,
  innerPadding: Dp = 2.dp,
  mainCellPadding: Dp = 1.dp,
  themeColor: ThemeColor,
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
              cellText = "메인목표",
              cellTextColor = cellTextColor,
              fontWeight = FontWeight.W700,
            )
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "Add Icon",
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
            cellText = "서브목표",
            cellTextColor = cellTextColor,
            fontWeight = fontWeight,
          )
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Icon",
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
          contentDescription = "Add Icon",
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
