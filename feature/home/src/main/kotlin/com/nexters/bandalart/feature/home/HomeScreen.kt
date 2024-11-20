@file:SuppressLint("StringFormatInvalid")

package com.nexters.bandalart.feature.home

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.bandalart.core.common.extension.externalShareForBitmap
import com.nexters.bandalart.core.common.utils.ObserveAsEvents
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray100
import com.nexters.bandalart.core.designsystem.theme.Gray50
import com.nexters.bandalart.core.ui.DevicePreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.ThemeColor
import com.nexters.bandalart.core.ui.component.BandalartDeleteAlertDialog
import com.nexters.bandalart.feature.home.model.BandalartUiModel
import com.nexters.bandalart.feature.home.model.dummyBandalartChartData
import com.nexters.bandalart.feature.home.model.dummyBandalartData
import com.nexters.bandalart.feature.home.model.dummyBandalartList
import com.nexters.bandalart.feature.home.ui.HomeHeader
import com.nexters.bandalart.feature.home.ui.HomeShareButton
import com.nexters.bandalart.feature.home.ui.HomeTopBar
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartChart
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartEmojiBottomSheet
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartListBottomSheet
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartSkeleton
import com.nexters.bandalart.feature.home.viewmodel.BottomSheetUiAction
import com.nexters.bandalart.feature.home.viewmodel.BottomSheetViewModel
import com.nexters.bandalart.feature.home.viewmodel.HomeUiAction
import com.nexters.bandalart.feature.home.viewmodel.HomeUiEvent
import com.nexters.bandalart.feature.home.viewmodel.HomeUiState
import com.nexters.bandalart.feature.home.viewmodel.HomeViewModel
import com.nexters.bandalart.feature.home.viewmodel.ModalType
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SnackbarDuration = 1000L

@Composable
internal fun HomeRoute(
    navigateToComplete: (Long, String, String) -> Unit,
    onShowSnackbar: suspend (String) -> Boolean,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    bottomSheetViewModel: BottomSheetViewModel = hiltViewModel(),
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bandalartCount by remember {
        derivedStateOf { uiState.bandalartList.size }
    }

    ObserveAsEvents(flow = homeViewModel.uiEvent) { event ->
        when (event) {
            is HomeUiEvent.NavigateToComplete -> {
                navigateToComplete(
                    event.id,
                    event.title,
                    event.profileEmoji.ifEmpty { context.getString(R.string.home_default_emoji) },
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

            is HomeUiEvent.ShareBandalart -> {
                context.externalShareForBitmap(event.bitmap)
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        bandalartCount = bandalartCount,
        onHomeUiAction = homeViewModel::onAction,
        onBottomSheetUiAction = bottomSheetViewModel::onAction,
        getBandalartList = homeViewModel::getBandalartList,
        getBandalart = homeViewModel::getBandalart,
        showSkeletonChanged = homeViewModel::updateSkeletonState,
        bottomSheetDataChanged = homeViewModel::updateBottomSheetData,
        setRecentBandalartId = homeViewModel::setRecentBandalartId,
        modifier = modifier,
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    bandalartCount: Int,
    onHomeUiAction: (HomeUiAction) -> Unit,
    onBottomSheetUiAction: (BottomSheetUiAction) -> Unit,
    getBandalartList: (Long?) -> Unit,
    getBandalart: (Long) -> Unit,
    showSkeletonChanged: (Boolean) -> Unit,
    bottomSheetDataChanged: (Boolean) -> Unit,
    setRecentBandalartId: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val graphicsLayer = rememberGraphicsLayer()

    LaunchedEffect(key1 = Unit) {
        getBandalartList(null)
    }

    LaunchedEffect(key1 = uiState.isBottomSheetDataChanged) {
        if (uiState.isBottomSheetDataChanged) {
            getBandalartList(null)
        }
    }

    LaunchedEffect(key1 = uiState.isShared) {
        if (uiState.isShared) {
            onHomeUiAction(HomeUiAction.ShareBandalart(graphicsLayer.toImageBitmap()))
        }
    }

    if (uiState.isBandalartListBottomSheetOpened) {
        uiState.bandalartData?.let { bandalart ->
            BandalartListBottomSheet(
                bandalartList = updateBandalartListTitles(uiState.bandalartList, context).toImmutableList(),
                currentBandalartId = bandalart.id,
                getBandalart = getBandalart,
                setRecentBandalartId = setRecentBandalartId,
                showSkeletonChanged = showSkeletonChanged,
                onDismissRequest = { onHomeUiAction(HomeUiAction.ToggleBandalartListBottomSheet(false)) },
                onAddClick = { onHomeUiAction(HomeUiAction.OnAddClick) },
                onBottomSheetUiAction = onBottomSheetUiAction,
            )
        }
    }

    if (uiState.isEmojiBottomSheetOpened) {
        uiState.bandalartData?.let { bandalart ->
            uiState.bandalartCellData?.let { cell ->
                BandalartEmojiBottomSheet(
                    bandalartId = bandalart.id,
                    cellId = cell.id,
                    currentEmoji = cell.profileEmoji,
                    onEmojiSelected = { bandalartId, cellId, emoji -> onHomeUiAction(HomeUiAction.OnEmojiSelected(bandalartId, cellId, emoji)) },
                    onResult = { bottomSheetState, bottomSheetDataChangedState ->
                        onHomeUiAction(HomeUiAction.ToggleEmojiBottomSheet(bottomSheetState))
                        bottomSheetDataChanged(bottomSheetDataChangedState)
                    },
                    onBottomSheetUiAction = onBottomSheetUiAction,
                )
            }
        }
    }

    if (uiState.isCellBottomSheetOpened) {
        uiState.bandalartData?.let { bandalart ->
            uiState.bandalartCellData?.let { cell ->
                BandalartBottomSheet(
                    bandalartId = bandalart.id,
                    isSubCell = false,
                    isMainCell = true,
                    isBlankCell = cell.title.isNullOrEmpty(),
                    cellData = cell,
                    onResult = { bottomSheetState, bottomSheetDataChangedState ->
                        onHomeUiAction(HomeUiAction.ToggleCellBottomSheet(bottomSheetState))
                        bottomSheetDataChanged(bottomSheetDataChangedState)
                    },
                )
            }
        }
    }

    if (uiState.isBandalartDeleteAlertDialogOpened) {
        uiState.bandalartData?.let { bandalart ->
            BandalartDeleteAlertDialog(
                title = if (bandalart.title.isNullOrEmpty()) {
                    stringResource(R.string.delete_bandalart_dialog_empty_title)
                } else {
                    stringResource(R.string.delete_bandalart_dialog_title, bandalart.title)
                },
                message = stringResource(R.string.delete_bandalart_dialog_message),
                onDeleteClicked = { onHomeUiAction(HomeUiAction.OnConfirmClick(ModalType.DELETE_DIALOG)) },
                onCancelClicked = { onHomeUiAction(HomeUiAction.OnCancelClick(ModalType.DELETE_DIALOG)) },
            )
        }
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
                    onListClick = { onHomeUiAction(HomeUiAction.OnListClick) },
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Gray100,
                )
                Column(
                    modifier = Modifier
                        .drawWithContent {
                            graphicsLayer.record { this@drawWithContent.drawContent() }
                            drawLayer(graphicsLayer)
                        },
                ) {
                    uiState.bandalartData?.let { bandalart ->
                        HomeHeader(
                            bandalartData = bandalart,
                            isDropDownMenuOpened = uiState.isDropDownMenuOpened,
                            onAction = onHomeUiAction,
                        )
                    }
                    if (uiState.bandalartCellData != null && uiState.bandalartData != null) {
                        BandalartChart(
                            bandalartId = uiState.bandalartData.id,
                            bandalartData = uiState.bandalartData,
                            bandalartCellData = uiState.bandalartCellData,
                            bottomSheetDataChanged = bottomSheetDataChanged,
                        )
                    }
                    Spacer(modifier = Modifier.height(64.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                HomeShareButton(
                    onShareButtonClick = { onHomeUiAction(HomeUiAction.OnShareButtonClick) },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }
            if (uiState.isShowSkeleton) {
                BandalartSkeleton()
            }
        }
    }
}

private fun updateBandalartListTitles(
    list: List<BandalartUiModel>,
    context: Context,
): List<BandalartUiModel> {
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

@DevicePreview
@Composable
private fun HomeScreenSingleBandalartPreview() {
    BandalartTheme {
        HomeScreen(
            uiState = HomeUiState(
                bandalartList = listOf(dummyBandalartList[0]).toImmutableList(),
                bandalartData = dummyBandalartData,
                bandalartCellData = dummyBandalartChartData,
            ),
            bandalartCount = listOf(dummyBandalartList[0]).size,
            onHomeUiAction = {},
            onBottomSheetUiAction = {},
            getBandalartList = {},
            getBandalart = {},
            showSkeletonChanged = {},
            bottomSheetDataChanged = {},
            setRecentBandalartId = {},
        )
    }
}

@DevicePreview
@Composable
private fun HomeScreenMultipleBandalartPreview() {
    BandalartTheme {
        HomeScreen(
            uiState = HomeUiState(
                bandalartList = dummyBandalartList.toImmutableList(),
                bandalartData = dummyBandalartData,
                bandalartCellData = dummyBandalartChartData,
            ),
            bandalartCount = dummyBandalartList.size,
            onHomeUiAction = {},
            onBottomSheetUiAction = {},
            getBandalartList = {},
            getBandalart = {},
            showSkeletonChanged = {},
            bottomSheetDataChanged = {},
            setRecentBandalartId = {},
        )
    }
}
