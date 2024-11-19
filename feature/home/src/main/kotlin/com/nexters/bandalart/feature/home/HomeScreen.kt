@file:SuppressLint("StringFormatInvalid")

package com.nexters.bandalart.feature.home

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.bandalart.core.common.utils.ObserveAsEvents
import com.nexters.bandalart.core.common.extension.externalShareForBitmap
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray100
import com.nexters.bandalart.core.designsystem.theme.Gray50
import com.nexters.bandalart.core.ui.DevicePreview
import com.nexters.bandalart.core.ui.ThemeColor
import com.nexters.bandalart.core.ui.component.BandalartDeleteAlertDialog
import com.nexters.bandalart.core.ui.component.LoadingIndicator
import com.nexters.bandalart.feature.home.model.BandalartDetailUiModel
import com.nexters.bandalart.feature.home.model.dummyBandalartChartData
import com.nexters.bandalart.feature.home.model.dummyBandalartDetailData
import com.nexters.bandalart.feature.home.model.dummyBandalartList
import com.nexters.bandalart.feature.home.ui.HomeHeader
import com.nexters.bandalart.feature.home.ui.HomeShareButton
import com.nexters.bandalart.feature.home.ui.HomeTopBar
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartChart
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartEmojiBottomSheet
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartListBottomSheet
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartSkeleton
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.nexters.bandalart.core.ui.R

private const val SnackbarDuration = 1000L

// TODO 서브셀을 지울 때 진짜 지우면 안됨 빈 값으로 초기화 하는게 맞을듯, 그 하위 셀들도 마찬가지
// TODO 현재 테스크 셀 들을 전부 완료 했을 때, 서브셀이 완료되지 않고 있는 듯
@Composable
internal fun HomeRoute(
    navigateToComplete: (Long, String, String) -> Unit,
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

    ObserveAsEvents(flow = viewModel.uiEvent) { event ->
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
        navigateToComplete = viewModel::navigateToComplete,
        getBandalartList = viewModel::getBandalartList,
        getBandalartDetail = viewModel::getBandalartDetail,
        createBandalart = viewModel::createBandalart,
        deleteBandalart = viewModel::deleteBandalart,
        showSkeletonChanged = viewModel::showSkeletonChanged,
        openDropDownMenu = viewModel::openDropDownMenu,
        openEmojiBottomSheet = viewModel::openEmojiBottomSheet,
        updateBandalartEmoji = viewModel::updateBandalartEmoji,
        openBandalartDeleteAlertDialog = viewModel::openBandalartDeleteAlertDialog,
        openCellBottomSheet = viewModel::openCellBottomSheet,
        bottomSheetDataChanged = viewModel::bottomSheetDataChanged,
        openBandalartListBottomSheet = viewModel::openBandalartListBottomSheet,
        setRecentBandalartId = viewModel::setRecentBandalartId,
        onShareButtonClick = viewModel::onShareButtonClick,
        shareBandalart = viewModel::shareBandalart,
        checkCompletedBandalartId = viewModel::checkCompletedBandalartId,
        modifier = modifier,
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    bandalartCount: Int,
    navigateToComplete: () -> Unit,
    getBandalartList: (Long?) -> Unit,
    getBandalartDetail: (Long) -> Unit,
    createBandalart: () -> Unit,
    deleteBandalart: (Long) -> Unit,
    showSkeletonChanged: (Boolean) -> Unit,
    openDropDownMenu: (Boolean) -> Unit,
    openEmojiBottomSheet: (Boolean) -> Unit,
    updateBandalartEmoji: (Long, Long, com.nexters.bandalart.feature.home.model.UpdateBandalartEmojiModel) -> Unit,
    openBandalartDeleteAlertDialog: (Boolean) -> Unit,
    openCellBottomSheet: (Boolean) -> Unit,
    bottomSheetDataChanged: (Boolean) -> Unit,
    openBandalartListBottomSheet: (Boolean) -> Unit,
    setRecentBandalartId: (Long) -> Unit,
    onShareButtonClick: () -> Unit,
    shareBandalart: (ImageBitmap) -> Unit,
    checkCompletedBandalartId: suspend (Long) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val graphicsLayer = rememberGraphicsLayer()

    LaunchedEffect(key1 = Unit) {
        getBandalartList(null)
    }

    LaunchedEffect(key1 = uiState.bandalartDetailData?.isCompleted) {
        uiState.bandalartDetailData?.let { detailData ->
            // 목표를 달성했을 경우
            if (detailData.isCompleted && !detailData.title.isNullOrEmpty()) {
                val isBandalartCompleted = checkCompletedBandalartId(detailData.id)
                // 목표 달성 화면을 띄워 줘야 하는 반다라트일 경우
                if (isBandalartCompleted) {
                    navigateToComplete()
                }
            }
        }
    }

    LaunchedEffect(key1 = uiState.isBottomSheetDataChanged) {
        if (uiState.isBottomSheetDataChanged) {
            getBandalartList(null)
        }
    }

    LaunchedEffect(key1 = uiState.isShared) {
        if (uiState.isShared) {
            shareBandalart(graphicsLayer.toImageBitmap())
        }
    }

    if (uiState.isBandalartListBottomSheetOpened) {
        uiState.bandalartDetailData?.let { detailData ->
            BandalartListBottomSheet(
                bandalartList = updateBandalartListTitles(uiState.bandalartList, context).toImmutableList(),
                currentBandalartId = detailData.id,
                getBandalartDetail = getBandalartDetail,
                setRecentBandalartId = setRecentBandalartId,
                showSkeletonChanged = showSkeletonChanged,
                onCancelClicked = { openBandalartListBottomSheet(false) },
                createBandalart = createBandalart,
            )
        }
    }

    if (uiState.isEmojiBottomSheetOpened) {
        uiState.bandalartDetailData?.let { detailData ->
            uiState.bandalartCellData?.let { cellData ->
                BandalartEmojiBottomSheet(
                    bandalartId = detailData.id,
                    cellId = cellData.id,
                    currentEmoji = cellData.profileEmoji,
                    updateBandalartEmoji = updateBandalartEmoji,
                    onResult = { bottomSheetState, bottomSheetDataChangedState ->
                        openEmojiBottomSheet(bottomSheetState)
                        bottomSheetDataChanged(bottomSheetDataChangedState)
                    },
                )
            }
        }
    }

    if (uiState.isCellBottomSheetOpened) {
        uiState.bandalartDetailData?.let { detailData ->
            uiState.bandalartCellData?.let { cellData ->
                BandalartBottomSheet(
                    bandalartId = detailData.id,
                    isSubCell = false,
                    isMainCell = true,
                    isBlankCell = cellData.title.isNullOrEmpty(),
                    cellData = cellData,
                    onResult = { bottomSheetState, bottomSheetDataChangedState ->
                        openCellBottomSheet(bottomSheetState)
                        bottomSheetDataChanged(bottomSheetDataChangedState)
                    },
                )
            }
        }
    }

    if (uiState.isBandalartDeleteAlertDialogOpened) {
        uiState.bandalartDetailData?.let { detailData ->
            BandalartDeleteAlertDialog(
                title = if (detailData.title.isNullOrEmpty()) {
                    stringResource(R.string.delete_bandalart_dialog_empty_title)
                } else {
                    stringResource(R.string.delete_bandalart_dialog_title, detailData.title)
                },
                message = stringResource(R.string.delete_bandalart_dialog_message),
                onDeleteClicked = { deleteBandalart(detailData.id) },
                onCancelClicked = { openBandalartDeleteAlertDialog(false) },
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
                    onShowBandalartList = { openBandalartListBottomSheet(true) },
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
                        }
                ) {
                    uiState.bandalartDetailData?.let { detail ->
                        HomeHeader(
                            bandalartDetailData = detail,
                            isDropDownMenuOpened = uiState.isDropDownMenuOpened,
                            openDropDownMenu = openDropDownMenu,
                            openEmojiBottomSheet = openEmojiBottomSheet,
                            openBandalartDeleteAlertDialog = openBandalartDeleteAlertDialog,
                            openCellBottomSheet = openCellBottomSheet,
                        )
                    }
                    if (uiState.bandalartCellData != null && uiState.bandalartDetailData != null) {
                        BandalartChart(
                            bandalartId = uiState.bandalartDetailData.id,
                            bandalartCellData = uiState.bandalartCellData,
                            themeColor = ThemeColor(
                                mainColor = uiState.bandalartDetailData.mainColor,
                                subColor = uiState.bandalartDetailData.subColor,
                            ),
                            bottomSheetDataChanged = bottomSheetDataChanged,
                        )
                    }
                    Spacer(modifier = Modifier.height(64.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                HomeShareButton(
                    onShareButtonClick = onShareButtonClick,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
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

@DevicePreview
@Composable
private fun HomeScreenSingleBandalartPreview() {
    BandalartTheme {
        HomeScreen(
            uiState = HomeUiState(
                bandalartList = listOf(dummyBandalartList[0]).toImmutableList(),
                bandalartDetailData = dummyBandalartDetailData,
                bandalartCellData = dummyBandalartChartData,
            ),
            bandalartCount = listOf(dummyBandalartList[0]).size,
            navigateToComplete = {},
            getBandalartList = {},
            getBandalartDetail = {},
            createBandalart = {},
            deleteBandalart = {},
            showSkeletonChanged = {},
            openDropDownMenu = {},
            openEmojiBottomSheet = {},
            updateBandalartEmoji = { _, _, _ -> },
            openBandalartDeleteAlertDialog = {},
            openCellBottomSheet = {},
            bottomSheetDataChanged = {},
            openBandalartListBottomSheet = {},
            setRecentBandalartId = {},
            onShareButtonClick = {},
            shareBandalart = {},
            checkCompletedBandalartId = { _ -> false },
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
                bandalartDetailData = dummyBandalartDetailData,
                bandalartCellData = dummyBandalartChartData,
            ),
            bandalartCount = dummyBandalartList.size,
            navigateToComplete = {},
            getBandalartList = {},
            getBandalartDetail = {},
            createBandalart = {},
            deleteBandalart = {},
            showSkeletonChanged = {},
            openDropDownMenu = {},
            openEmojiBottomSheet = {},
            updateBandalartEmoji = { _, _, _ -> },
            openBandalartDeleteAlertDialog = {},
            openCellBottomSheet = {},
            bottomSheetDataChanged = {},
            openBandalartListBottomSheet = {},
            setRecentBandalartId = {},
            onShareButtonClick = {},
            shareBandalart = {},
            checkCompletedBandalartId = { _ -> false },
        )
    }
}
