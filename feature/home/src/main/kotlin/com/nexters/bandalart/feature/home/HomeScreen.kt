package com.nexters.bandalart.feature.home

import android.app.Activity
import android.content.Context
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
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.nexters.bandalart.core.common.extension.await
import com.nexters.bandalart.core.common.utils.UiText
import com.nexters.bandalart.core.common.utils.isValidImmediateAppUpdate
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray100
import com.nexters.bandalart.core.designsystem.theme.Gray50
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.core.ui.DevicePreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.component.BandalartDeleteAlertDialog
import com.nexters.bandalart.feature.home.HomeScreen.Event
import com.nexters.bandalart.feature.home.model.BandalartUiModel
import com.nexters.bandalart.feature.home.model.CellType
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
import com.nexters.bandalart.feature.home.viewmodel.ModalType
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import timber.log.Timber

// TODO Action 와 Event 를 분리하지 말고 바로 Event 를 호출하면 될듯 (이게 MVI 가 맞나?)
@Parcelize
data object HomeScreen : Screen {
    data class State(
        val bandalartList: ImmutableList<BandalartUiModel> = persistentListOf(),
        val bandalartData: BandalartUiModel? = null,
        val bandalartCellData: BandalartCellEntity? = null,
        val isDropDownMenuOpened: Boolean = false,
        val isBandalartDeleteAlertDialogOpened: Boolean = false,
        val isBandalartListBottomSheetOpened: Boolean = false,
        val isCellBottomSheetOpened: Boolean = false,
        val isEmojiBottomSheetOpened: Boolean = false,
        val isBandalartCompleted: Boolean = false,
        val isShowSkeleton: Boolean = false,
        val isShared: Boolean = false,
        val isCaptured: Boolean = false,
        val bandalartChartUrl: String? = null,
        val clickedCellType: CellType = CellType.MAIN,
        val clickedCellData: BandalartCellEntity? = null,
        val isUpdateAlreadyRejected: Boolean = false,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data class NavigateToComplete(
            val id: Long,
            val title: String,
            val profileEmoji: String,
            val bandalartChartImageUri: String,
        ) : Event

        data class ShowSnackbar(val message: UiText) : Event
        data class ShowToast(val message: UiText) : Event
        data class SaveBandalart(val bitmap: ImageBitmap) : Event
        data class ShareBandalart(val bitmap: ImageBitmap) : Event
        data class CaptureBandalart(val bitmap: ImageBitmap) : Event
        data object ShowAppVersion : Event
        data class SaveLastRejectedUpdateVersion(val versionCode: Int) : Event
        data object OnListClick : Event
        data object OnSaveClick : Event
        data object OnDeleteClick : Event
        data class OnEmojiSelected(
            val bandalartId: Long,
            val cellId: Long,
            val updateBandalartEmojiModel: UpdateBandalartEmojiEntity,
        ) : Event

        data class OnConfirmClick(val modalType: ModalType) : Event
        data class OnCancelClick(val modalType: ModalType) : Event
        data object OnShareButtonClick : Event
        data object OnAddClick : Event
        data class ToggleDropDownMenu(val flag: Boolean) : Event
        data class ToggleDeleteAlertDialog(val flag: Boolean) : Event
        data class ToggleEmojiBottomSheet(val flag: Boolean) : Event
        data class ToggleCellBottomSheet(val flag: Boolean) : Event
        data class ToggleBandalartListBottomSheet(val flag: Boolean) : Event
        data class OnBandalartListItemClick(val key: Long) : Event
        data class OnBandalartCellClick(
            val cellType: CellType,
            val isMainCellTitleEmpty: Boolean,
            val cellData: BandalartCellEntity,
        ) : Event

        data object OnCloseButtonClick : Event
        data object OnAppTitleClick : Event
    }
}

// TODO 서브 셀을 먼저 채워야 태스크 셀을 채울 수 있도록 validation 추가
// TODO UiAction(Intent) 과 UiEvent(SideEffect) 를 명확하게 분리
// TODO 텍스트를 컴포저블로 각각 분리하지 말고, 폰트를 적용하는 방식으로 변경
//@Suppress("TooGenericExceptionCaught")
//@Composable
//internal fun HomeRoute(
//    navigateToComplete: (Long, String, String, String) -> Unit,
//    onShowSnackbar: suspend (String) -> Boolean,
//    modifier: Modifier = Modifier,
//    homeViewModel: HomeViewModel = hiltViewModel(),
//) {
//    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//    val snackbarHostState = remember { SnackbarHostState() }
//    val appVersion = remember {
//        try {
//            context.packageManager.getPackageInfo(context.packageName, 0).versionName
//        } catch (e: PackageManager.NameNotFoundException) {
//            Timber.tag("AppVersion").e(e, "Failed to get package info")
//            "Unknown"
//        }
//    }
//
//    val appUpdateManager = remember { AppUpdateManagerFactory.create(context) }
//
//    val installStateUpdatedListener = remember {
//        InstallStateUpdatedListener { state ->
//            if (state.installStatus() == InstallStatus.DOWNLOADED) {
//                scope.launch {
//                    val snackbarResult = snackbarHostState.showSnackbar(
//                        message = context.getString(R.string.update_ready_to_install),
//                        actionLabel = context.getString(R.string.update_action_restart),
//                        duration = Indefinite,
//                    )
//
//                    // 재시작 버튼 클릭시
//                    if (snackbarResult == SnackbarResult.ActionPerformed) {
//                        appUpdateManager.completeUpdate()
//                    }
//                }
//            }
//        }
//    }
//
//    DisposableEffect(Unit) {
//        appUpdateManager.registerListener(installStateUpdatedListener)
//        onDispose {
//            appUpdateManager.unregisterListener(installStateUpdatedListener)
//        }
//    }
//
//    val appUpdateResultLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartIntentSenderForResult(),
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_CANCELED && result.data != null) {
//            scope.launch {
//                appUpdateManager.appUpdateInfo.await().availableVersionCode().let { versionCode ->
//                    homeViewModel.setLastRejectedUpdateVersion(versionCode)
//                }
//            }
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        try {
//            val appUpdateInfo = appUpdateManager.appUpdateInfo.await()
//
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
//                val availableVersionCode = appUpdateInfo.availableVersionCode()
//                if (!isValidImmediateAppUpdate(availableVersionCode) &&
//                    !homeViewModel.isUpdateAlreadyRejected(availableVersionCode) &&
//                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
//                ) {
//                    appUpdateManager.startUpdateFlowForResult(
//                        appUpdateInfo,
//                        appUpdateResultLauncher,
//                        AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
//                    )
//                }
//            }
//        } catch (e: Exception) {
//            Timber.e(e, "Failed to check for flexible update")
//        }
//    }
//
//    // TODO 굳이 derivedStateOf 를 사용하지 않아도 될 것 같음(쓸꺼면 제대로)
//    val bandalartCount by remember {
//        derivedStateOf { uiState.bandalartList.size }
//    }
//
//    ObserveAsEvents(flow = homeViewModel.uiEvent) { event ->
//        when (event) {
//            is HomeUiEvent.NavigateToComplete -> {
//                navigateToComplete(
//                    event.id,
//                    event.title,
//                    event.profileEmoji.ifEmpty { context.getString(R.string.home_default_emoji) },
//                    event.bandalartChart,
//                )
//            }
//
//            is HomeUiEvent.ShowSnackbar -> {
//                scope.launch {
//                    val job = launch {
//                        onShowSnackbar(event.message.asString(context))
//                    }
//                    delay(SnackbarDuration)
//                    job.cancel()
//                }
//            }
//
//            is HomeUiEvent.ShowToast -> {
//                Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
//            }
//
//            is HomeUiEvent.SaveBandalart -> {
//                context.saveImageToGallery(event.bitmap)
//                Toast.makeText(context, context.getString(R.string.save_bandalart_image), Toast.LENGTH_SHORT).show()
//            }
//
//            is HomeUiEvent.ShareBandalart -> {
//                context.externalShareForBitmap(event.bitmap)
//            }
//
//            is HomeUiEvent.CaptureBandalart -> {
//                homeViewModel.updateBandalartChartUrl(context.bitmapToFileUri(event.bitmap).toString())
//            }
//
//            is HomeUiEvent.ShowAppVersion -> {
//                Toast.makeText(context, context.getString(R.string.app_version_info, appVersion), Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    HomeScreen(
//        uiState = uiState,
//        bandalartCount = bandalartCount,
//        onHomeUiAction = homeViewModel::onAction,
//        shareBandalart = homeViewModel::shareBandalart,
//        captureBandalart = homeViewModel::captureBandalart,
//        saveBandalart = homeViewModel::saveBandalartImage,
//        snackbarHostState = snackbarHostState,
//        modifier = modifier,
//    )
//}

@CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Home(
    state: HomeScreen.State,
//    onHomeUiAction: (HomeUiAction) -> Unit,
//    shareBandalart: (ImageBitmap) -> Unit,
//    captureBandalart: (ImageBitmap) -> Unit,
//    saveBandalart: (ImageBitmap) -> Unit,
//    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val overlayHost = LocalOverlayHost.current
    val homeGraphicsLayer = rememberGraphicsLayer()
    val completeGraphicsLayer = rememberGraphicsLayer()

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
                    // homeViewModel.setLastRejectedUpdateVersion(versionCode)
                    // eventSink(Event.SaveLastRejectedUpdateVersion(versionCode))
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
                    // !homeViewModel.isUpdateAlreadyRejected(availableVersionCode) &&
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

    LaunchedEffect(key1 = state.isShared) {
        if (state.isShared) {
            // shareBandalart(homeGraphicsLayer.toImageBitmap())
            eventSink(Event.ShareBandalart(homeGraphicsLayer.toImageBitmap()))
        }
    }

    LaunchedEffect(key1 = state.isCaptured) {
        if (state.isCaptured) {
            if (state.isBandalartCompleted) {
                // captureBandalart(completeGraphicsLayer.toImageBitmap())
                eventSink(Event.CaptureBandalart(homeGraphicsLayer.toImageBitmap()))
            } else {
                // saveBandalart(completeGraphicsLayer.toImageBitmap())
                eventSink(Event.SaveBandalart(homeGraphicsLayer.toImageBitmap()))
            }
        }
    }

    if (state.isBandalartListBottomSheetOpened) {
        state.bandalartData?.let { bandalart ->
            BandalartListBottomSheet(
                bandalartList = updateBandalartListTitles(state.bandalartList, context).toImmutableList(),
                currentBandalartId = bandalart.id,
                eventSink = eventSink,
            )
        }
    }

    if (state.isEmojiBottomSheetOpened) {
        state.bandalartData?.let { bandalart ->
            state.bandalartCellData?.let { cell ->
                BandalartEmojiBottomSheet(
                    bandalartId = bandalart.id,
                    cellId = cell.id,
                    currentEmoji = bandalart.profileEmoji,
                    eventSink = eventSink,
                )
            }
        }
    }

    if (state.isCellBottomSheetOpened) {
        state.bandalartData?.let { bandalart ->
            state.clickedCellData?.let { cell ->
                BandalartBottomSheet(
                    bandalartId = bandalart.id,
                    cellType = state.clickedCellType,
                    isBlankCell = cell.title.isNullOrEmpty(),
                    cellData = cell,
                    eventSink = eventSink,
                )
            }
        }
    }

    if (state.isBandalartDeleteAlertDialogOpened) {
        state.bandalartData?.let { bandalart ->
            BandalartDeleteAlertDialog(
                title = if (bandalart.title.isNullOrEmpty()) stringResource(R.string.delete_bandalart_dialog_empty_title)
                else stringResource(R.string.delete_bandalart_dialog_title, bandalart.title),
                message = stringResource(R.string.delete_bandalart_dialog_message),
                onDeleteClicked = {
                    eventSink(Event.OnConfirmClick(ModalType.DELETE_DIALOG))
                },
                onCancelClicked = {
                    eventSink(Event.OnCancelClick(ModalType.DELETE_DIALOG))
                },
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
                    bandalartCount = state.bandalartList.size,
                    eventSink = eventSink,
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
                    if (state.bandalartCellData != null && state.bandalartData != null) {
                        HomeHeader(
                            bandalartData = state.bandalartData,
                            isDropDownMenuOpened = state.isDropDownMenuOpened,
                            cellData = state.bandalartCellData,
                            eventSink = eventSink,
                        )
                        BandalartChart(
                            bandalartData = state.bandalartData,
                            bandalartCellData = state.bandalartCellData,
                            eventSink = eventSink,
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
                    onShareButtonClick = {
                        eventSink(Event.OnShareButtonClick)
//                        scope.launch {
//                            eventSink(Event.ShareBandalart(homeGraphicsLayer.toImageBitmap()))
//                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
            )

            if (state.isShowSkeleton) {
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
        Home(
            state = HomeScreen.State(
                bandalartList = listOf(dummyBandalartList[0]).toImmutableList(),
                bandalartData = dummyBandalartData,
                bandalartCellData = dummyBandalartChartData,
                eventSink = {}
            ),
        )
    }
}

@DevicePreview
@Composable
private fun HomeScreenMultipleBandalartPreview() {
    BandalartTheme {
        Home(
            state = HomeScreen.State(
                bandalartList = dummyBandalartList.toImmutableList(),
                bandalartData = dummyBandalartData,
                bandalartCellData = dummyBandalartChartData,
                eventSink = {}
            ),
        )
    }
}
