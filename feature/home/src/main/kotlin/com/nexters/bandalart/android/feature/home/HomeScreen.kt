@file:OptIn(ExperimentalMaterial3Api::class)
@file:SuppressLint("StringFormatInvalid")

package com.nexters.bandalart.android.feature.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.component.BandalartDeleteAlertDialog
import com.nexters.bandalart.android.core.ui.component.BandalartDropDownMenu
import com.nexters.bandalart.android.core.ui.component.EmojiText
import com.nexters.bandalart.android.core.ui.component.FixedSizeText
import com.nexters.bandalart.android.core.ui.component.LoadingScreen
import com.nexters.bandalart.android.core.ui.component.NetworkErrorAlertDialog
import com.nexters.bandalart.android.core.ui.extension.ThemeColor
import com.nexters.bandalart.android.core.ui.extension.clickableSingle
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.extension.toColor
import com.nexters.bandalart.android.core.ui.extension.toFormatDate
import com.nexters.bandalart.android.core.ui.theme.Gray100
import com.nexters.bandalart.android.core.ui.theme.Gray300
import com.nexters.bandalart.android.core.ui.theme.Gray50
import com.nexters.bandalart.android.core.ui.theme.Gray600
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.MainColor
import com.nexters.bandalart.android.feature.home.model.BandalartDetailUiModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartEmojiModel
import com.nexters.bandalart.android.feature.home.ui.BandalartChart
import com.nexters.bandalart.android.feature.home.ui.BandalartEmojiPicker
import com.nexters.bandalart.android.feature.home.ui.BandalartListBottomSheet
import com.nexters.bandalart.android.feature.home.ui.BandalartSkeleton
import com.nexters.bandalart.android.feature.home.ui.CompletionRatioProgressBar
import com.nexters.bandalart.android.feature.home.ui.HomeTopBar
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
    navigateToComplete = { key, title, emoji -> navigateToComplete(key, title, emoji) },
    getBandalartList = { key -> viewModel.getBandalartList(key) },
    getBandalartDetail = viewModel::getBandalartDetail,
    updateBandalartEmoji = viewModel::updateBandalartEmoji,
    createBandalart = viewModel::createBandalart,
    deleteBandalart = viewModel::deleteBandalart,
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
          bandalartCount = uiState.bandalartList.size,
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
                    .clickable { openEmojiBottomSheet(true) },
                  contentAlignment = Alignment.Center,
                ) {
                  if (uiState.bandalartDetailData?.profileEmoji.isNullOrEmpty()) {
                    val image = painterResource(
                      id = com.nexters.bandalart.android.core.designsystem.R.drawable.ic_empty_emoji,
                    )
                    Image(
                      painter = image,
                      contentDescription = stringResource(R.string.empty_emoji_descrption),
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
                  contentDescription = stringResource(R.string.edit_descrption),
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
                text = uiState.bandalartDetailData?.title ?: stringResource(R.string.home_empty_title),
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
                contentDescription = stringResource(R.string.option_descrption),
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
              text = stringResource(
                R.string.home_complete_ratio,
                uiState.bandalartDetailData?.completionRatio ?: 0,
              ),
              color = Gray600,
              fontWeight = FontWeight.W500,
              fontSize = 12.sp,
              letterSpacing = (-0.24).sp,
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
                    contentDescription = stringResource(R.string.check_descrption),
                    tint = Gray900,
                    modifier = Modifier.size(13.dp),
                  )
                  FixedSizeText(
                    text = stringResource(R.string.home_complete),
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
            .clickableSingle { uiState.bandalartDetailData?.let { shareBandalart(it.key) } }
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
              contentDescription = stringResource(R.string.share_descrption),
            )
            FixedSizeText(
              text = stringResource(R.string.home_share),
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
