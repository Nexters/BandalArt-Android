@file:SuppressLint("StringFormatInvalid")

package com.nexters.bandalart.android.feature.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.bandalart.android.core.designsystem.theme.Gray100
import com.nexters.bandalart.android.core.designsystem.theme.Gray50
import com.nexters.bandalart.android.core.ui.ObserveAsEvents
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.ThemeColor
import com.nexters.bandalart.android.core.ui.component.BandalartDeleteAlertDialog
import com.nexters.bandalart.android.core.ui.component.LoadingIndicator
import com.nexters.bandalart.android.core.ui.component.NetworkErrorAlertDialog
import com.nexters.bandalart.android.feature.home.model.BandalartDetailUiModel
import com.nexters.bandalart.android.feature.home.ui.HomeHeader
import com.nexters.bandalart.android.feature.home.ui.HomeTopBar
import com.nexters.bandalart.android.feature.home.ui.ShareButton
import com.nexters.bandalart.android.feature.home.ui.bandalart.BandalartChart
import com.nexters.bandalart.android.feature.home.ui.bandalart.BandalartEmojiBottomSheet
import com.nexters.bandalart.android.feature.home.ui.bandalart.BandalartListBottomSheet
import com.nexters.bandalart.android.feature.home.ui.bandalart.BandalartSkeleton
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SnackbarDuration = 1000L

@Composable
internal fun HomeRoute(
  navigateToComplete: (String, String, String) -> Unit,
  onShowSnackbar: suspend (String) -> Boolean,
  modifier: Modifier = Modifier,
  viewModel: HomeViewModel = hiltViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val bandalartCount by remember {
    derivedStateOf { uiState.bandalartList.size }
  }

  ObserveAsEvents(flow = viewModel.eventFlow) { event ->
    when (event) {
      is HomeUiEvent.NavigateToComplete -> {
        navigateToComplete(
          event.key,
          event.title,
          event.profileEmoji.ifEmpty {
            context.getString(R.string.home_default_emoji)
          },
        )
      }

      is HomeUiEvent.ShowSnackbar -> {
        scope.launch {
          val job = launch {
            onShowSnackbar(event.message.asString(context))
          }
          delay(SnackbarDuration)
          job.cancel()
        }
      }

      is HomeUiEvent.ShowToast -> {
        Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
      }
    }
  }

  HomeScreen(
    uiState = uiState,
    bandalartCount = bandalartCount,
    navigateToComplete = viewModel::navigateToComplete,
    getBandalartList = viewModel::getBandalartList,
    getBandalartDetail = viewModel::getBandalartDetail,
    createBandalart = viewModel::createBandalart,
    deleteBandalart = viewModel::deleteBandalart,
    // loadingChanged = { state -> viewModel.loadingChanged(state) },
    showSkeletonChanged = viewModel::showSkeletonChanged,
    openDropDownMenu = viewModel::openDropDownMenu,
    openEmojiBottomSheet = viewModel::openEmojiBottomSheet,
    openBandalartDeleteAlertDialog = viewModel::openBandalartDeleteAlertDialog,
    openCellBottomSheet = viewModel::openCellBottomSheet,
    bottomSheetDataChanged = viewModel::bottomSheetDataChanged,
    openBandalartListBottomSheet = viewModel::openBandalartListBottomSheet,
    setRecentBandalartKey = viewModel::setRecentBandalartKey,
    shareBandalart = viewModel::shareBandalart,
    initShareUrl = viewModel::initShareUrl,
    checkCompletedBandalartKey = viewModel::checkCompletedBandalartKey,
    openNetworkErrorDialog = viewModel::openNetworkErrorAlertDialog,
    modifier = modifier,
  )
}

// TODO HomeHeader 에서 처럼 삭제 다이얼로그에 해당 셀의 타이틀 정보를 전달해야 함
@Composable
internal fun HomeScreen(
  uiState: HomeUiState,
  bandalartCount: Int,
  navigateToComplete: () -> Unit,
  getBandalartList: (String?) -> Unit,
  getBandalartDetail: (String) -> Unit,
  createBandalart: () -> Unit,
  deleteBandalart: (String) -> Unit,
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
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current

  LaunchedEffect(key1 = Unit) {
    getBandalartList(null)
  }

  LaunchedEffect(key1 = uiState.bandalartDetailData?.isCompleted) {
    val bandalartDetailData = uiState.bandalartDetailData ?: return@LaunchedEffect
    // 목표를 달성했을 경우
    if (bandalartDetailData.isCompleted && !bandalartDetailData.title.isNullOrEmpty()) {
      // 목표 달성 화면을 띄워 줘야 하는 반다라트일 경우
      val isBandalartCompleted = checkCompletedBandalartKey(bandalartDetailData.key)
      if (isBandalartCompleted) {
        navigateToComplete()
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

  if (uiState.isBandalartListBottomSheetOpened) {
    BandalartListBottomSheet(
      bandalartList = updateBandalartListTitles(uiState.bandalartList, context).toImmutableList(),
      currentBandalartKey = uiState.bandalartDetailData!!.key,
      getBandalartDetail = getBandalartDetail,
      setRecentBandalartKey = setRecentBandalartKey,
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
        HomeHeader(
          uiState = uiState,
          openDropDownMenu = openDropDownMenu,
          openEmojiBottomSheet = openEmojiBottomSheet,
          openBandalartDeleteAlertDialog = openBandalartDeleteAlertDialog,
          openCellBottomSheet = openCellBottomSheet,
        )
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
        ShareButton(
          modifier = Modifier.align(Alignment.CenterHorizontally),
          uiState = uiState,
          shareBandalart = shareBandalart,
        )
      }
      when {
        uiState.isLoading -> {
          LoadingIndicator()
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
