@file:OptIn(ExperimentalMaterial3Api::class)
@file:SuppressLint("StringFormatInvalid")

package com.nexters.bandalart.android.feature.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.nexters.bandalart.android.core.ui.R
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
import com.nexters.bandalart.android.core.ui.component.BandalartDeleteAlertDialog
import com.nexters.bandalart.android.core.ui.component.BandalartDropDownMenu
import com.nexters.bandalart.android.core.ui.component.CellText
import com.nexters.bandalart.android.core.ui.component.EmojiText
import com.nexters.bandalart.android.core.ui.component.FixedSizeText
import com.nexters.bandalart.android.core.ui.component.LoadingScreen
import com.nexters.bandalart.android.core.ui.component.NetworkErrorAlertDialog
import com.nexters.bandalart.android.core.ui.extension.ThemeColor
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
import com.nexters.bandalart.android.core.ui.theme.MainColor
import com.nexters.bandalart.android.core.ui.theme.White
import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.android.feature.home.model.BandalartDetailUiModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartEmojiModel
import com.nexters.bandalart.android.feature.home.ui.BandalartEmojiPicker
import com.nexters.bandalart.android.feature.home.ui.BandalartListBottomSheet
import com.nexters.bandalart.android.feature.home.ui.BandalartSkeleton
import com.nexters.bandalart.android.feature.home.ui.CompletionRatioProgressBar
import com.nexters.bandalart.android.feature.home.ui.HomeTopBar
import timber.log.Timber

@Composable
internal fun HomeRoute(
  modifier: Modifier = Modifier,
  navigateToComplete: (String, String, String) -> Unit,
  navigateToOnBoarding: () -> Unit,
  onShowSnackbar: suspend (String) -> Boolean,
  viewModel: HomeViewModel = hiltViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current

  LaunchedEffect(viewModel) {
    viewModel.snackbarMessage.collect { message ->
      onShowSnackbar(message.asString(context))
    }
  }
  LaunchedEffect(viewModel) {
    viewModel.logMessage.collect { message ->
      Timber.e(message.asString(context))
    }
  }
  LaunchedEffect(viewModel) {
    viewModel.toastMessage.collect { message ->
      Toast.makeText(context, message.asString(context), Toast.LENGTH_SHORT).show()
    }
  }

  HomeScreen(
    modifier = modifier,
    uiState = uiState,
    navigateToComplete = { key, title, emoji -> navigateToComplete(key, title, emoji) },
    getBandalartList = { key -> viewModel.getBandalartList(key) },
    getBandalartDetail = viewModel::getBandalartDetail,
    updateBandalartEmoji = viewModel::updateBandalartEmoji,
    createBandalart = viewModel::createBandalart,
    deleteBandalart = viewModel::deleteBandalart,
    loadingChanged = { state -> viewModel.loadingChanged(state) },
    showSkeletonChanged = { state -> viewModel.showSkeletonChanged(state) },
    openDropDownMenu = { state -> viewModel.openDropDownMenu(state) },
    openEmojiBottomSheet = { state -> viewModel.openEmojiBottomSheet(state) },
    openBandalartDeleteAlertDialog = { state -> viewModel.openBandalartDeleteAlertDialog(state) },
    openCellBottomSheet = { state -> viewModel.openCellBottomSheet(state) },
    bottomSheetDataChanged = { state -> viewModel.bottomSheetDataChanged(state) },
    openBandalartListBottomSheet = { state -> viewModel.openBandalartListBottomSheet(state) },
    setRecentBandalartKey = { key -> viewModel.setRecentBandalartKey(key) },
    shareBandalart = { key -> viewModel.shareBandalart(key) },
    initShareUrl = viewModel::initShareUrl,
    navigateToOnBoarding = navigateToOnBoarding,
    checkCompletedBandalartKey = { key -> viewModel.checkCompletedBandalartKey(key) },
    openNetworkErrorDialog = { state -> viewModel.openNetworkErrorAlertDialog(state) },
  )
}

@Composable
internal fun HomeScreen(
  modifier: Modifier = Modifier,
  uiState: HomeUiState,
  navigateToComplete: (String, String, String) -> Unit,
  getBandalartList: (String?) -> Unit,
  getBandalartDetail: (String) -> Unit,
  updateBandalartEmoji: (String, String, UpdateBandalartEmojiModel) -> Unit,
  createBandalart: () -> Unit,
  deleteBandalart: (String) -> Unit,
  loadingChanged: (Boolean) -> Unit,
  showSkeletonChanged: (Boolean) -> Unit,
  openDropDownMenu: (Boolean) -> Unit,
  openEmojiBottomSheet: (Boolean) -> Unit,
  openBandalartDeleteAlertDialog: (Boolean) -> Unit,
  openCellBottomSheet: (Boolean) -> Unit,
  bottomSheetDataChanged: (Boolean) -> Unit,
  openBandalartListBottomSheet: (Boolean) -> Unit,
  setRecentBandalartKey: (String) -> Unit,
  shareBandalart: (String) -> Unit,
  initShareUrl: () -> Unit,
  navigateToOnBoarding: () -> Unit,
  checkCompletedBandalartKey: suspend (String) -> Boolean,
  openNetworkErrorDialog: (Boolean) -> Unit,
) {
  val context = LocalContext.current

  // TODO null 를 파라미터로 넣어줘야 하는 이유 학습
  LaunchedEffect(key1 = Unit) {
    getBandalartList(null)
  }

  // TODO 매번 목표 달성 화면으로 이동하지 않도록
  LaunchedEffect(key1 = uiState.bandalartDetailData?.isCompleted) {
    // 목표를 달성했을 경우
    if (uiState.bandalartDetailData?.isCompleted == true) {
      // 목표 달성 화면을 띄워 줘야 하는 반다라트일 경우
      if (uiState.bandalartDetailData.key.let { checkCompletedBandalartKey(it) }) {
        navigateToComplete(
          uiState.bandalartDetailData.key,
          uiState.bandalartDetailData.title!!,
          if (uiState.bandalartDetailData.profileEmoji.isNullOrEmpty()) {
            context.getString(R.string.homescreen_default_emoji_text)
          } else uiState.bandalartDetailData.profileEmoji,
        )
      }
    }
  }

  LaunchedEffect(key1 = uiState.isBottomSheetDataChanged) {
    if (uiState.isBottomSheetDataChanged) {
      loadingChanged(true)
      getBandalartList(null)
    }
  }

  LaunchedEffect(key1 = uiState.shareUrl) {
    if (uiState.shareUrl.isNotEmpty()) {
      val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
          Intent.EXTRA_TEXT,
          context.getString(R.string.homescreen_share_url_text, uiState.shareUrl),
        )
        type = context.getString(R.string.homescreen_share_type_text)
      }
      val shareIntent = Intent.createChooser(sendIntent, null)
      context.startActivity(shareIntent)
      initShareUrl()
    }
  }

  if (uiState.isBandalartListBottomSheetOpened) {
    BandalartListBottomSheet(
      bandalartList = updateBandalartListTitles(uiState.bandalartList, context),
      currentBandalartKey = uiState.bandalartDetailData!!.key,
      getBandalartDetail = getBandalartDetail,
      setRecentBandalartKey = setRecentBandalartKey,
      showSkeletonChanged = showSkeletonChanged,
      onCancelClicked = { openBandalartListBottomSheet(false) },
      createBandalart = createBandalart,
    )
  }

  if (uiState.isEmojiBottomSheetOpened) {
    val emojiPickerState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
      onDismissRequest = { openEmojiBottomSheet(false) },
      modifier = Modifier.wrapContentSize(),
      sheetState = emojiPickerState,
      content = BandalartEmojiPicker(
        currentEmoji = uiState.bandalartCellData?.profileEmoji,
        isBottomSheet = true,
        onResult = { currentEmojiResult, openEmojiBottomSheetResult ->
          openEmojiBottomSheet(openEmojiBottomSheetResult)
          updateBandalartEmoji(
            uiState.bandalartDetailData!!.key,
            uiState.bandalartCellData!!.key,
            UpdateBandalartEmojiModel(profileEmoji = currentEmojiResult),
          )
        },
        emojiPickerScope = rememberCoroutineScope(),
        emojiPickerState = emojiPickerState,
      ),
      dragHandle = null,
    )
  }

  if (uiState.isCellBottomSheetOpened) {
    BandalartBottomSheet(
      bandalartKey = uiState.bandalartDetailData!!.key,
      isSubCell = false,
      isMainCell = true,
      isBlankCell = uiState.bandalartCellData!!.title.isNullOrEmpty(),
      cellData = uiState.bandalartCellData,
      onResult = { bottomSheetState, bottomSheetDataChangedState ->
        openCellBottomSheet(bottomSheetState)
        bottomSheetDataChanged(bottomSheetDataChangedState)
      },
    )
  }

  if (uiState.isBandalartDeleteAlertDialogOpened) {
    BandalartDeleteAlertDialog(
      title = if (uiState.bandalartDetailData?.title.isNullOrEmpty()) {
        context.getString(R.string.delete_bandalart_dialog_empty_title_text)
      } else {
        context.getString(R.string.delete_bandalart_dialog_title_text, uiState.bandalartDetailData?.title)
      },
      message = context.getString(R.string.delete_bandalart_dialog_message_text),
      onDeleteClicked = { uiState.bandalartDetailData?.let { deleteBandalart(it.key) } },
      onCancelClicked = { openBandalartDeleteAlertDialog(false) },
    )
  }

  if (uiState.isNetworkErrorAlertDialogOpened) {
    NetworkErrorAlertDialog(
      title = context.getString(R.string.network_error_dialog_title_text),
      message = context.getString(R.string.network_error_dialog_message_text),
      onConfirmClick = {
        openNetworkErrorDialog(false)
        loadingChanged(true)
        getBandalartList(null)
      },
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
          .verticalScroll(rememberScrollState())
          .padding(bottom = 32.dp),
      ) {
        HomeTopBar(
          bandalartCount = uiState.bandalartList.size,
          onShowBandalartList = { openBandalartListBottomSheet(true) },
          onLogoClicked = {
            if (uiState.bandalartDetailData?.title != null) {
              navigateToComplete(
                uiState.bandalartDetailData.key,
                uiState.bandalartDetailData.title,
                if (uiState.bandalartDetailData.profileEmoji.isNullOrEmpty()) {
                  context.getString(R.string.homescreen_default_emoji_text)
                } else uiState.bandalartDetailData.profileEmoji,
              )
            }
          },
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
                    .clickable { openEmojiBottomSheet(true) },
                  contentAlignment = Alignment.Center,
                ) {
                  if (uiState.bandalartDetailData?.profileEmoji.isNullOrEmpty()) {
                    val image = painterResource(
                      id = com.nexters.bandalart.android.core.designsystem.R.drawable.ic_empty_emoji,
                    )
                    Image(
                      painter = image,
                      contentDescription = context.getString(R.string.empty_emoji_descrption_text),
                    )
                  } else {
                    EmojiText(
                      emojiText = uiState.bandalartDetailData?.profileEmoji,
                      fontSize = 22.sp,
                    )
                  }
                }
              }
              if (uiState.bandalartDetailData?.profileEmoji.isNullOrEmpty()) {
                val image = painterResource(
                  id = com.nexters.bandalart.android.core.designsystem.R.drawable.ic_edit,
                )
                Image(
                  painter = image,
                  contentDescription = context.getString(R.string.edit_descrption_text),
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
              FixedSizeText(
                text = uiState.bandalartDetailData?.title ?: context.getString(R.string.homescreen_empty_title_text),
                color = if (uiState.bandalartDetailData?.title.isNullOrEmpty()) Gray300 else Gray900,
                fontWeight = FontWeight.W700,
                fontSize = 20.sp,
                letterSpacing = (-0.4).sp,
                modifier = Modifier
                  .align(Alignment.Center)
                  .clickable { openCellBottomSheet(true) },
              )
              val image = painterResource(
                id = com.nexters.bandalart.android.core.designsystem.R.drawable.ic_option,
              )
              Image(
                painter = image,
                contentDescription = context.getString(R.string.option_descrption_text),
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
            // TODO 온보딩 navigation 제거
            FixedSizeText(
              text = context.getString(
                R.string.homescreen_complete_ratio_text,
                uiState.bandalartDetailData?.completionRatio ?: 0,
              ),
              color = Gray600,
              fontWeight = FontWeight.W500,
              fontSize = 12.sp,
              letterSpacing = (-0.24).sp,
              modifier = Modifier.clickable { navigateToOnBoarding() },
            )
            if (!uiState.bandalartDetailData?.dueDate.isNullOrEmpty()) {
              VerticalDivider(
                modifier = Modifier
                  .height(8.dp)
                  .padding(start = 6.dp),
                thickness = 1.dp,
                color = Gray300,
              )
              FixedSizeText(
                text = uiState.bandalartDetailData?.dueDate!!.toFormatDate(),
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
                  .background(color = uiState.bandalartDetailData.mainColor.toColor()),
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 9.dp),
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = context.getString(R.string.check_descrption_text),
                    tint = Gray900,
                    modifier = Modifier.size(13.dp),
                  )
                  FixedSizeText(
                    text = context.getString(R.string.homescreen_complete_text),
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
            progressColor = (uiState.bandalartCellData?.mainColor?.toColor() ?: MainColor),
          )
          Spacer(modifier = Modifier.height(18.dp))
        }
        when {
          uiState.bandalartCellData != null && uiState.bandalartDetailData != null -> {
            BandalartChart(
              bandalartKey = uiState.bandalartDetailData.key,
              uiState = uiState,
              themeColor = ThemeColor(
                mainColor = uiState.bandalartDetailData.mainColor,
                subColor = uiState.bandalartDetailData.subColor,
              ),
              bottomSheetDataChanged = bottomSheetDataChanged,
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
            .clickable { uiState.bandalartDetailData?.let { shareBandalart(it.key) } }
            .align(Alignment.CenterHorizontally),
          contentAlignment = Alignment.Center,
        ) {
          Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 20.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            val image = painterResource(
              id = com.nexters.bandalart.android.core.designsystem.R.drawable.ic_share,
            )
            Image(
              painter = image,
              contentDescription = context.getString(R.string.share_descrption_text),
            )
            FixedSizeText(
              text = context.getString(R.string.homescreen_share_text),
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

private fun updateBandalartListTitles(
  list: List<BandalartDetailUiModel>,
  context: Context,
): List<BandalartDetailUiModel> {
  var counter = 1
  return list.map { item ->
    if (item.title.isNullOrEmpty()) {
      val updatedTitle = context.getString(R.string.bandalart_list_empty_title_text, counter)
      counter += 1
      item.copy(
        title = updatedTitle,
        isGeneratedTitle = true,
      )
    } else {
      item
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
            CellGrid(
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
          Cell(
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

@Composable
fun CellGrid(
  bandalartKey: String,
  themeColor: ThemeColor,
  subCell: SubCell,
  rows: Int,
  cols: Int,
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
            modifier = Modifier.weight(1f),
            bandalartKey = bandalartKey,
            themeColor = themeColor,
            isMainCell = false,
            cellInfo = CellInfo(
              isSubCell = isSubCell,
              colIndex = colIndex,
              rowIndex = rowIndex,
              colCnt = cols,
              rowCnt = rows,
            ),
            cellData = if (isSubCell) subCell.bandalartChartData
            else subCell.bandalartChartData.children[taskIndex++],
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
  val context = LocalContext.current
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
              cellText = context.getString(R.string.homescreen_main_cell_text),
              cellTextColor = cellTextColor,
              fontWeight = FontWeight.W700,
            )
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = context.getString(R.string.add_descrption_text),
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
            cellText = context.getString(R.string.homescreen_sub_cell_text),
            cellTextColor = cellTextColor,
            fontWeight = fontWeight,
          )
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = context.getString(R.string.add_descrption_text),
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
          contentDescription = context.getString(R.string.add_descrption_text),
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
              contentDescription = context.getString(R.string.complete_descrption_text),
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
