@file:OptIn(ExperimentalMaterial3Api::class)
@file:SuppressLint("StringFormatInvalid")

package com.nexters.bandalart.android.feature.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.component.BandalartDeleteAlertDialog
import com.nexters.bandalart.android.core.ui.component.LoadingScreen
import com.nexters.bandalart.android.core.ui.component.NetworkErrorAlertDialog
import com.nexters.bandalart.android.core.ui.extension.ThemeColor
import com.nexters.bandalart.android.core.designsystem.theme.Gray100
import com.nexters.bandalart.android.core.designsystem.theme.Gray50
import com.nexters.bandalart.android.feature.home.model.BandalartDetailUiModel
import com.nexters.bandalart.android.feature.home.ui.BandalartChart
import com.nexters.bandalart.android.feature.home.ui.BandalartEmojiBottomSheet
import com.nexters.bandalart.android.feature.home.ui.BandalartListBottomSheet
import com.nexters.bandalart.android.feature.home.ui.BandalartSkeleton
import com.nexters.bandalart.android.feature.home.ui.HomeHeader
import com.nexters.bandalart.android.feature.home.ui.HomeTopBar
import com.nexters.bandalart.android.feature.home.ui.ShareButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SnackbarDuration = 1000L

@Composable
internal fun HomeRoute(
  modifier: Modifier = Modifier,
  navigateToComplete: (String, String, String) -> Unit,
  onShowSnackbar: suspend (String) -> Boolean,
  viewModel: HomeViewModel = hiltViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current
  val bandalartCount by derivedStateOf { uiState.bandalartList.size }

  LaunchedEffect(viewModel) {
    viewModel.eventFlow.collect { event ->
      when (event) {
        is HomeUiEvent.ShowSnackbar -> {
          val job = launch {
            onShowSnackbar(event.message.asString(context))
          }
          delay(SnackbarDuration)
          job.cancel()
        }
        is HomeUiEvent.ShowToast -> {
          Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  HomeScreen(
    modifier = modifier,
    uiState = uiState,
    bandalartCount = bandalartCount,
    navigateToComplete = { key, title, emoji -> navigateToComplete(key, title, emoji) },
    settingDestination = viewModel::setDestinationKey,
    getBandalartList = { key -> viewModel.getBandalartList(key) },
    getBandalartDetail = viewModel::getBandalartDetail,
    createBandalart = viewModel::createBandalart,
    deleteBandalart = viewModel::deleteBandalart,
    findBandalartIdx = viewModel::findPagerIdx,
    setBandalartIdx = viewModel::setBandalartIdx,
    // loadingChanged = { state -> viewModel.loadingChanged(state) },
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
    checkCompletedBandalartKey = { key -> viewModel.checkCompletedBandalartKey(key) },
    openNetworkErrorDialog = { state -> viewModel.openNetworkErrorAlertDialog(state) },
  )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeScreen(
  modifier: Modifier = Modifier,
  uiState: HomeUiState,
  bandalartCount: Int,
  navigateToComplete: (String, String, String) -> Unit,
  settingDestination: (String) -> Unit,
  getBandalartList: (String?) -> Unit,
  getBandalartDetail: (String) -> Unit,
  createBandalart: () -> Unit,
  deleteBandalart: (String) -> Unit,
  findBandalartIdx: (String?) -> Int,
  setBandalartIdx: (Int) -> Unit,
  // loadingChanged: (Boolean) -> Unit,
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
  checkCompletedBandalartKey: suspend (String) -> Boolean,
  openNetworkErrorDialog: (Boolean) -> Unit,
) {
  val context = LocalContext.current
  val animationScope = rememberCoroutineScope()
  val pagerState = rememberPagerState(initialPage = 0, pageCount = { bandalartCount })

  LaunchedEffect(key1 = Unit) {
    getBandalartList(null)
  }

  LaunchedEffect(key1 = uiState.bandalartDetailData?.isCompleted) {
    // 목표를 달성했을 경우
    if (uiState.bandalartDetailData?.isCompleted == true && !uiState.bandalartDetailData.title.isNullOrEmpty()) {
      // 목표 달성 화면을 띄워 줘야 하는 반다라트일 경우
      val isBandalartCompleted = checkCompletedBandalartKey(uiState.bandalartDetailData.key)
      if (isBandalartCompleted) {
        navigateToComplete(
          uiState.bandalartDetailData.key,
          uiState.bandalartDetailData.title,
          if (uiState.bandalartDetailData.profileEmoji.isNullOrEmpty()) {
            context.getString(R.string.home_default_emoji)
          } else uiState.bandalartDetailData.profileEmoji,
        )
      }
    }
  }

  LaunchedEffect(key1 = uiState.isBottomSheetDataChanged) {
    if (uiState.isBottomSheetDataChanged) {
      // loadingChanged(true)
      getBandalartList(null)
    }
  }

  LaunchedEffect(key1 = uiState.shareUrl) {
    if (uiState.shareUrl.isNotEmpty()) {
      val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
          Intent.EXTRA_TEXT,
          context.getString(R.string.home_share_url, uiState.shareUrl),
        )
        type = context.getString(R.string.home_share_type)
      }
      val shareIntent = Intent.createChooser(sendIntent, null)
      context.startActivity(shareIntent)
      initShareUrl()
    }
  }

  LaunchedEffect(uiState.destinationKey, uiState.bandalartList) {
    if (uiState.destinationKey != "over" && uiState.bandalartList.size != 0) {
      setBandalartIdx(findBandalartIdx(uiState.destinationKey))
    }
  }

  LaunchedEffect(uiState.pagerIdx, uiState.destinationKey) {
    if (uiState.pagerIdx != -1 && uiState.destinationKey != "over") {
      animationScope.launch {
        pagerState.animateScrollToPage(uiState.pagerIdx)
      }
    }
  }

  LaunchedEffect(pagerState, uiState.bandalartList) {
    snapshotFlow { pagerState.currentPage }.collect { page ->
      if (uiState.bandalartList.size != 0) {
        if (uiState.destinationKey == uiState.bandalartList[page].key) {
          settingDestination("over")
        }
        getBandalartDetail(uiState.bandalartList[page].key)
      }
    }
  }

  if (uiState.isBandalartListBottomSheetOpened) {
    BandalartListBottomSheet(
      bandalartList = updateBandalartListTitles(uiState.bandalartList, context),
      currentBandalartKey = uiState.bandalartDetailData!!.key,
      getBandalartDetail = getBandalartDetail,
      setRecentBandalartKey = setRecentBandalartKey,
      moveSelectedBandalart = {
        animationScope.launch {
          pagerState.animateScrollToPage(findBandalartIdx(it))
        }
      },
      showSkeletonChanged = showSkeletonChanged,
      onCancelClicked = { openBandalartListBottomSheet(false) },
      createBandalart = createBandalart,
    )
  }

  if (uiState.isEmojiBottomSheetOpened) {
    BandalartEmojiBottomSheet(
      bandalartKey = uiState.bandalartDetailData!!.key,
      cellKey = uiState.bandalartCellData!!.key,
      currentEmoji = uiState.bandalartCellData.profileEmoji,
      onResult = { bottomSheetState, bottomSheetDataChangedState ->
        openEmojiBottomSheet(bottomSheetState)
        bottomSheetDataChanged(bottomSheetDataChangedState)
      },
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
        stringResource(R.string.delete_bandalart_dialog_empty_title)
      } else {
        stringResource(R.string.delete_bandalart_dialog_title, uiState.bandalartDetailData?.title ?: "")
      },
      message = stringResource(R.string.delete_bandalart_dialog_message),
      onDeleteClicked = { uiState.bandalartDetailData?.let { deleteBandalart(it.key) } },
      onCancelClicked = { openBandalartDeleteAlertDialog(false) },
    )
  }

  if (uiState.isNetworkErrorAlertDialogOpened) {
    NetworkErrorAlertDialog(
      title = stringResource(R.string.network_error_dialog_title),
      message = stringResource(R.string.network_error_dialog_message),
      onConfirmClick = {
        openNetworkErrorDialog(false)
        // loadingChanged(true)
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
          bandalartCount = bandalartCount,
          onShowBandalartList = { openBandalartListBottomSheet(true) },
        )
        HorizontalDivider(
          thickness = 1.dp,
          color = Gray100,
        )
        when {
          uiState.bandalartDetailData != null && uiState.bandalartCellData != null -> {
            HorizontalPager(state = pagerState) { page ->
              Column(modifier = Modifier.fillMaxSize()) {
                HomeHeader(
                  uiState = uiState,
                  openDropDownMenu = openDropDownMenu,
                  openEmojiBottomSheet = openEmojiBottomSheet,
                  openBandalartDeleteAlertDialog = openBandalartDeleteAlertDialog,
                  openCellBottomSheet = openCellBottomSheet,
                )
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
          }
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(
          modifier = Modifier
            .height(50.dp)
            .align(Alignment.CenterHorizontally),
          horizontalArrangement = Arrangement.Center,
        ) {
          repeat(uiState.bandalartList.size) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(
              modifier = Modifier
                .padding(2.dp)
                .clip(CircleShape)
                .background(color)
                .size(5.dp),
            )
          }
        }

        Spacer(modifier = Modifier.height(64.dp))
        Spacer(modifier = Modifier.weight(1f))
        ShareButton(
          modifier = Modifier.align(Alignment.CenterHorizontally),
          uiState = uiState,
          shareBandalart = shareBandalart,
        )
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
      val updatedTitle = context.getString(R.string.bandalart_list_empty_title, counter)
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
