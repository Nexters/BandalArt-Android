package com.nexters.bandalart.feature.home

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.nexters.bandalart.core.common.extension.await
import com.nexters.bandalart.core.common.extension.bitmapToFileUri
import com.nexters.bandalart.core.common.extension.externalShareForBitmap
import com.nexters.bandalart.core.common.extension.saveImageToGallery
import com.nexters.bandalart.core.common.utils.ObserveAsEvents
import com.nexters.bandalart.core.common.utils.isValidImmediateAppUpdate
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray100
import com.nexters.bandalart.core.designsystem.theme.Gray50
import com.nexters.bandalart.core.ui.DevicePreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.component.BandalartDeleteAlertDialog
import com.nexters.bandalart.feature.home.model.BandalartUiModel
import com.nexters.bandalart.feature.home.model.dummy.dummyBandalartChartData
import com.nexters.bandalart.feature.home.model.dummy.dummyBandalartData
import com.nexters.bandalart.feature.home.model.dummy.dummyBandalartList
import com.nexters.bandalart.feature.home.ui.HomeHeader
import com.nexters.bandalart.feature.home.ui.HomeShareButton
import com.nexters.bandalart.feature.home.ui.HomeTopBar
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartChart
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartEmojiBottomSheet
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartListBottomSheet
import com.nexters.bandalart.feature.home.ui.bandalart.BandalartSkeleton
import com.nexters.bandalart.feature.home.viewmodel.BottomSheetModal
import com.nexters.bandalart.feature.home.viewmodel.DialogModal
import com.nexters.bandalart.feature.home.viewmodel.HomeUiAction
import com.nexters.bandalart.feature.home.viewmodel.HomeUiEvent
import com.nexters.bandalart.feature.home.viewmodel.HomeUiState
import com.nexters.bandalart.feature.home.viewmodel.HomeViewModel
import com.nexters.bandalart.feature.home.viewmodel.ModalState
import com.nexters.bandalart.feature.home.viewmodel.ModalType
import com.nexters.bandalart.feature.home.viewmodel.ShareState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

private const val SnackbarDuration = 1500L

// TODO 서브 셀을 먼저 채워야 태스크 셀을 채울 수 있도록 validation 추가
// TODO UiAction(Intent) 과 UiEvent(SideEffect) 를 명확하게 분리
// TODO 텍스트를 컴포저블로 각각 분리하지 말고, 폰트를 적용하는 방식으로 변경
// TODO 셀 삭제 다이얼로그를 띄우면 셀 바텀시트가 내려가는 문제 해결해야함!
@Suppress("TooGenericExceptionCaught")
@Composable
internal fun HomeRoute(
    navigateToComplete: (Long, String, String, String) -> Unit,
    onShowSnackbar: suspend (String) -> Boolean,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val appVersion = remember {
        try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.tag("AppVersion").e(e, "Failed to get package info")
            "Unknown"
        }
    }

    val appUpdateManager = remember { AppUpdateManagerFactory.create(context) }

    val installStateUpdatedListener = remember {
        InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                scope.launch {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = context.getString(R.string.update_ready_to_install),
                        actionLabel = context.getString(R.string.update_action_restart),
                        duration = Indefinite,
                    )

                    // 재시작 버튼 클릭시
                    if (snackbarResult == SnackbarResult.ActionPerformed) {
                        appUpdateManager.completeUpdate()
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        appUpdateManager.registerListener(installStateUpdatedListener)
        onDispose {
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
    }

    val appUpdateResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_CANCELED && result.data != null) {
            scope.launch {
                appUpdateManager.appUpdateInfo.await().availableVersionCode().let { versionCode ->
                    homeViewModel.setLastRejectedUpdateVersion(versionCode)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        try {
            val appUpdateInfo = appUpdateManager.appUpdateInfo.await()

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                val availableVersionCode = appUpdateInfo.availableVersionCode()
                if (!isValidImmediateAppUpdate(availableVersionCode) &&
                    !homeViewModel.isUpdateAlreadyRejected(availableVersionCode) &&
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        appUpdateResultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
                    )
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to check for flexible update")
        }
    }

    ObserveAsEvents(flow = homeViewModel.uiEvent) { event ->
        when (event) {
            is HomeUiEvent.NavigateToComplete -> {
                navigateToComplete(
                    event.id,
                    event.title,
                    event.profileEmoji.ifEmpty { context.getString(R.string.home_default_emoji) },
                    event.bandalartChart,
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

            is HomeUiEvent.SaveBandalart -> {
                context.saveImageToGallery(event.bitmap)
                Toast.makeText(context, context.getString(R.string.save_bandalart_image), Toast.LENGTH_SHORT).show()
            }

            is HomeUiEvent.ShareBandalart -> {
                context.externalShareForBitmap(event.bitmap)
            }

            is HomeUiEvent.CaptureBandalart -> {
                homeViewModel.updateBandalartChartUrl(context.bitmapToFileUri(event.bitmap).toString())
            }

            is HomeUiEvent.ShowAppVersion -> {
                Toast.makeText(context, context.getString(R.string.app_version_info, appVersion), Toast.LENGTH_SHORT).show()
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        bandalartCount = (uiState as? HomeUiState.Content)?.bandalartList?.size ?: 0,
        onHomeUiAction = homeViewModel::onAction,
        shareBandalart = homeViewModel::shareBandalart,
        captureBandalart = homeViewModel::captureBandalart,
        saveBandalart = homeViewModel::saveBandalartImage,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    bandalartCount: Int,
    onHomeUiAction: (HomeUiAction) -> Unit,
    shareBandalart: (ImageBitmap) -> Unit,
    captureBandalart: (ImageBitmap) -> Unit,
    saveBandalart: (ImageBitmap) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val homeGraphicsLayer = rememberGraphicsLayer()
    val completeGraphicsLayer = rememberGraphicsLayer()

    when (uiState) {
        is HomeUiState.Content -> {
            // Share 상태 처리
            LaunchedEffect(key1 = uiState.shareState) {
                when (uiState.shareState) {
                    ShareState.Share -> shareBandalart(homeGraphicsLayer.toImageBitmap())
                    ShareState.Capture -> {
                        if (uiState.isBandalartCompleted) {
                            captureBandalart(completeGraphicsLayer.toImageBitmap())
                        } else {
                            saveBandalart(completeGraphicsLayer.toImageBitmap())
                        }
                    }

                    ShareState.None -> {}
                }
            }

            // 모달 처리
            when (val modal = uiState.modal) {
                is ModalState.Modals -> {
                    modal.bottomSheet?.let { bottomSheet ->
                        when (bottomSheet) {
                            is BottomSheetModal.BandalartList -> {
                                uiState.bandalartData?.let { bandalart ->
                                    BandalartListBottomSheet(
                                        bandalartList = updateBandalartListTitles(uiState.bandalartList, context).toImmutableList(),
                                        currentBandalartId = bandalart.id,
                                        onHomeUiAction = onHomeUiAction,
                                    )
                                }
                            }

                            is BottomSheetModal.Emoji -> {
                                uiState.bandalartData?.let { bandalart ->
                                    uiState.bandalartCellData?.let { cell ->
                                        BandalartEmojiBottomSheet(
                                            bandalartId = bandalart.id,
                                            cellId = cell.id,
                                            currentEmoji = bandalart.profileEmoji,
                                            onHomeUiAction = onHomeUiAction,
                                        )
                                    }
                                }
                            }

                            is BottomSheetModal.Cell -> {
                                uiState.bandalartData?.let { bandalart ->
                                    uiState.clickedCellData?.let { cell ->
                                        BandalartBottomSheet(
                                            bandalartId = bandalart.id,
                                            cellType = uiState.clickedCellType,
                                            isBlankCell = cell.title.isNullOrEmpty(),
                                            cellData = cell,
                                            onHomeUiAction = onHomeUiAction,
                                            bottomSheetData = bottomSheet.data,
                                        )
                                    }
                                }
                            }
                        }
                    }

                    modal.dialog?.let { dialog ->
                        when (dialog) {
                            DialogModal.Delete -> {
                                uiState.bandalartData?.let { bandalart ->
                                    BandalartDeleteAlertDialog(
                                        title = if (bandalart.title.isNullOrEmpty()) {
                                            stringResource(R.string.delete_bandalart_dialog_empty_title)
                                        } else {
                                            stringResource(R.string.delete_bandalart_dialog_title, bandalart.title)
                                        },
                                        message = stringResource(R.string.delete_bandalart_dialog_message),
                                        onDeleteClicked = {
                                            onHomeUiAction(HomeUiAction.OnConfirmClick(ModalType.DELETE_DIALOG))
                                        },
                                        onCancelClicked = {
                                            onHomeUiAction(HomeUiAction.OnCancelClick(ModalType.DELETE_DIALOG))
                                        },
                                    )
                                }
                            }
                        }
                    }
                }

                ModalState.DropDownMenu -> {
                    // HomeHeader 내부에서 처리
                }

                ModalState.Hidden -> {}
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
                            onHomeUiAction = onHomeUiAction,
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Gray100,
                        )
                        Column(
                            modifier = Modifier
                                .drawWithContent {
                                    homeGraphicsLayer.record { this@drawWithContent.drawContent() }
                                    drawLayer(homeGraphicsLayer)
                                }
                                .background(Gray50),
                        ) {
                            if (uiState.bandalartCellData != null && uiState.bandalartData != null) {
                                HomeHeader(
                                    bandalartData = uiState.bandalartData,
                                    cellData = uiState.bandalartCellData,
                                    isDropDownMenuOpened = uiState.modal == ModalState.DropDownMenu,
                                    onAction = onHomeUiAction,
                                )
                                BandalartChart(
                                    bandalartData = uiState.bandalartData,
                                    bandalartCellData = uiState.bandalartCellData,
                                    onHomeUiAction = onHomeUiAction,
                                    modifier = Modifier
                                        .drawWithContent {
                                            completeGraphicsLayer.record { this@drawWithContent.drawContent() }
                                            drawLayer(completeGraphicsLayer)
                                        }
                                        .background(Gray50),
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

                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.align(Alignment.BottomCenter),
                    )

                    if (uiState.isShowSkeleton) {
                        BandalartSkeleton()
                    }
                }
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
            uiState = HomeUiState.Content(
                bandalartList = listOf(dummyBandalartList[0]).toImmutableList(),
                bandalartData = dummyBandalartData,
                bandalartCellData = dummyBandalartChartData,
            ),
            bandalartCount = listOf(dummyBandalartList[0]).size,
            onHomeUiAction = {},
            shareBandalart = {},
            captureBandalart = {},
            saveBandalart = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}

@DevicePreview
@Composable
private fun HomeScreenMultipleBandalartPreview() {
    BandalartTheme {
        HomeScreen(
            uiState = HomeUiState.Content(
                bandalartList = dummyBandalartList.toImmutableList(),
                bandalartData = dummyBandalartData,
                bandalartCellData = dummyBandalartChartData,
            ),
            bandalartCount = dummyBandalartList.size,
            onHomeUiAction = {},
            shareBandalart = {},
            captureBandalart = {},
            saveBandalart = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}
