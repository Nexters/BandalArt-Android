package com.nexters.bandalart.feature.home.presenter

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.nexters.bandalart.core.common.extension.bitmapToFileUri
import com.nexters.bandalart.core.common.extension.externalShareForBitmap
import com.nexters.bandalart.core.common.extension.saveImageToGallery
import com.nexters.bandalart.core.common.utils.UiText
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.core.domain.repository.InAppUpdateRepository
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.complete.CompleteScreen
import com.nexters.bandalart.feature.home.HomeScreen
import com.nexters.bandalart.feature.home.mapper.toUiModel
import com.nexters.bandalart.feature.home.model.BandalartUiModel
import com.nexters.bandalart.feature.home.model.CellType
import com.nexters.bandalart.feature.home.viewmodel.HomeUiEvent
import com.nexters.bandalart.feature.home.viewmodel.HomeUiState
import com.nexters.bandalart.feature.home.viewmodel.ModalType
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import com.nexters.bandalart.feature.home.HomeScreen.State
import com.nexters.bandalart.feature.home.HomeScreen.Event
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

// TODO Presenter 에 context 못쓰지 않나? 여기서 이벤트 구현하는게 맞나? -> 쓸 수 있음
// TODO Navigation 을 app 모듈 또는 main 모듈에서 전역으로 관리하는게 아니다보니, feature 모듈간에 순환참조가 발생할 것 같은데...
// TODO Intent 와 SideEffect 가 구분되지 않는다... 어쩌지
class HomePresenter @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    @Assisted private val navigator: Navigator,
    private val bandalartRepository: BandalartRepository,
    private val inAppUpdateRepository: InAppUpdateRepository,
) : Presenter<State> {

    @Composable
    override fun present(): State {
        val scope = rememberStableCoroutineScope()

        var uiState by rememberRetained { mutableStateOf(State(eventSink = {})) }
        val eventChannel = rememberRetained { Channel<Event>() }

        LaunchedEffect(Unit) {
            uiState = getBandalartList(uiState)
        }

        LaunchedEffect(Unit) {
            bandalartRepository.getBandalartList()
                .map { list -> list.map { it.toUiModel() } }
                .filterNotNull()
                .distinctUntilChanged()
                .collect { bandalartList ->
                    uiState = uiState.copy(bandalartList = bandalartList.toImmutableList())
                }
        }

        // 완료 상태 관찰
        LaunchedEffect(Unit) {
            uiState.bandalartData?.let { initialBandalart ->
                bandalartRepository.getBandalartList()
                    .map { list -> list.map { it.toUiModel() } }
                    .filterNotNull()
                    .distinctUntilChanged()
                    .collect { bandalartList ->
                        val completedBandalart = bandalartList.find { it.id == initialBandalart.id }
                        completedBandalart?.let { bandalart ->
                            if (bandalart.isCompleted && !bandalart.title.isNullOrEmpty()) {
                                val isBandalartCompleted = bandalartRepository.checkCompletedBandalartId(bandalart.id)
                                if (isBandalartCompleted) {
                                    delay(500L)
                                    uiState = uiState.copy(isCaptured = true)
                                    delay(500L)
                                    eventChannel.send(
                                        Event.NavigateToComplete(
                                            id = bandalart.id,
                                            title = bandalart.title,
                                            profileEmoji = bandalart.profileEmoji.orEmpty(),
                                            bandalartChartImageUri = uiState.bandalartChartUrl.orEmpty(),
                                        )
                                    )
                                }
                            }
                        }
                    }
            }
        }

        val snackbarHostState = remember { SnackbarHostState() }
        val appVersion = remember {
            try {
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                Timber.tag("AppVersion").e(e, "Failed to get package info")
                "Unknown"
            }
        }

        var bandalartCaptureUrl by remember { mutableStateOf("") }
        val isUpdateAlreadyRejected by remember { mutableStateOf(false) }
        val bandalartData by remember { mutableStateOf(BandalartUiModel()) }

        // 이벤트 수신 처리
        LaunchedEffect(Unit) {
            eventChannel.receiveAsFlow().collect { event ->
                when (event) {
                    is Event.NavigateToComplete -> {
                        navigator.goTo(CompleteScreen(
                            bandalartId = event.id,
                            bandalartTitle = event.title,
                            bandalartProfileEmoji = event.profileEmoji,
                            bandalartChartImageUri = event.bandalartChartImageUri
                        ))
                    }
                    is Event.ShowSnackbar -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(event.message.asString(context))
                        }
                    }
                    is Event.ShowToast -> {
                        Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
                    }
                    is Event.SaveBandalart -> {
                        context.saveImageToGallery(event.bitmap)
                        Toast.makeText(context, context.getString(R.string.save_bandalart_image), Toast.LENGTH_SHORT).show()
                    }
                    is Event.ShareBandalart -> {
                        context.externalShareForBitmap(event.bitmap)
                    }
                    is Event.CaptureBandalart -> {
                        uiState = uiState.copy(
                            bandalartChartUrl = context.bitmapToFileUri(event.bitmap).toString()
                        )
                    }
                    is Event.ShowAppVersion -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.app_version_info, appVersion),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {} // 나머지 이벤트는 eventSink에서 처리
                }
            }
        }

        val eventSink: (Event) -> Unit = { event ->
            scope.launch {
                when (event) {
                    is Event.OnListClick -> {
                        uiState = uiState.copy(isBandalartListBottomSheetOpened = true)
                    }

                    is Event.OnSaveClick -> {
                        uiState = uiState.copy(isCaptured = true)
                    }

                    is Event.OnDeleteClick -> {
                        uiState = uiState.copy(
                            isBandalartDeleteAlertDialogOpened = true,
                            isDropDownMenuOpened = false
                        )
                    }

                    is Event.OnEmojiSelected -> {
                        bandalartRepository.updateBandalartEmoji(
                            event.bandalartId,
                            event.cellId,
                            event.updateBandalartEmojiModel
                        )
                        uiState = uiState.copy(isEmojiBottomSheetOpened = false)
                    }

                    is Event.OnConfirmClick -> {
                        when (event.modalType) {
                            ModalType.DELETE_DIALOG -> uiState.bandalartData?.let {
                                bandalartRepository.deleteBandalart(it.id)
                                uiState = uiState.copy(isBandalartDeleteAlertDialogOpened = false)
                                eventChannel.send(Event.ShowSnackbar(UiText.StringResource(R.string.delete_bandalart)))
                            }
                            else -> {}
                        }
                    }

                    is Event.OnCancelClick -> {
                        uiState = when (event.modalType) {
                            ModalType.EMOJI -> uiState.copy(isEmojiBottomSheetOpened = false)
                            ModalType.BANDALART_LIST -> uiState.copy(isBandalartListBottomSheetOpened = false)
                            ModalType.DELETE_DIALOG -> uiState.copy(isBandalartDeleteAlertDialogOpened = false)
                            else -> uiState
                        }
                    }

                    is Event.OnShareButtonClick -> {
                        uiState = uiState.copy(isShared = true)
                    }

                    is Event.OnAddClick -> {
                        if (uiState.bandalartList.size + 1 > 5) {
                            eventChannel.send(Event.ShowToast(UiText.StringResource(R.string.limit_create_bandalart)))
                            return@launch
                        }

                        bandalartRepository.createBandalart()?.let { bandalart ->
                            uiState = uiState.copy(isBandalartListBottomSheetOpened = false)
                            // 새로운 반다라트를 생성하면 화면에 생성된 반다라트 표를 보여주도록 id를 전달
                            uiState = getBandalart(uiState, bandalart.id)  // currentState 파라미터 추가
                            // 새로운 반다라트의 키를 최근에 확인한 반다라트로 저장
                            bandalartRepository.setRecentBandalartId(bandalart.id)
                            bandalartRepository.upsertBandalartId(bandalart.id, false)
                            eventChannel.send(Event.ShowSnackbar(UiText.StringResource(R.string.create_bandalart)))
                        }
                    }

                    is Event.ToggleDropDownMenu -> {
                        uiState = uiState.copy(isDropDownMenuOpened = event.flag)
                    }

                    is Event.ToggleDeleteAlertDialog -> {
                        uiState = uiState.copy(isBandalartDeleteAlertDialogOpened = event.flag)
                    }

                    is Event.ToggleEmojiBottomSheet -> {
                        uiState = uiState.copy(isEmojiBottomSheetOpened = event.flag)
                    }

                    is Event.ToggleCellBottomSheet -> {
                        if (!event.flag) {
                            uiState = uiState.copy(isCellBottomSheetOpened = false)
                        } else {
                            uiState = when (uiState.clickedCellType) {
                                CellType.MAIN -> uiState.copy(
                                    isCellBottomSheetOpened = true,
                                    clickedCellType = CellType.MAIN
                                )
                                CellType.SUB -> uiState.copy(
                                    isCellBottomSheetOpened = true,
                                    clickedCellType = CellType.SUB
                                )
                                else -> uiState.copy(
                                    isCellBottomSheetOpened = true,
                                    clickedCellType = CellType.TASK
                                )
                            }
                        }
                    }

                    is Event.ToggleBandalartListBottomSheet -> {
                        uiState = uiState.copy(isBandalartListBottomSheetOpened = event.flag)
                    }

                    is Event.OnBandalartListItemClick -> {
                        bandalartRepository.setRecentBandalartId(event.key)
                        getBandalart(uiState, event.key)
                        uiState = uiState.copy(isBandalartListBottomSheetOpened = false)
                    }

                    is Event.OnBandalartCellClick -> {
                        when {
                            event.cellType != CellType.MAIN && event.isMainCellTitleEmpty -> {
                                eventChannel.send(Event.ShowToast(UiText.StringResource(R.string.please_input_main_goal)))
                            }
                            else -> {
                                uiState = uiState.copy(
                                    clickedCellData = event.cellData,
                                    isCellBottomSheetOpened = true,
                                    clickedCellType = event.cellType
                                )
                            }
                        }
                    }

                    is Event.OnCloseButtonClick -> {
                        uiState = uiState.copy(isCellBottomSheetOpened = false)
                    }

                    is Event.OnAppTitleClick -> {
                        eventChannel.send(Event.ShowAppVersion)
                    }

                    else -> {}
                }
            }
        }

        return uiState.copy(eventSink = eventSink)
    }

    private suspend fun getBandalartList(currentState: State): State {
        val bandalartList = bandalartRepository.getBandalartList()
            .map { list -> list.map { it.toUiModel() } }
            .first()

        // 이전 반다라트 목록 상태 조회
        val prevBandalartList = bandalartRepository.getPrevBandalartList()

        // 새로 업데이트 된 상태와 이전 상태를 비교
        val completedKeys = bandalartList.filter { bandalart ->
            val prevBandalart = prevBandalartList.find { it.first == bandalart.id }
            prevBandalart != null && !prevBandalart.second && bandalart.isCompleted
        }.map { it.id }

        // 이번에 목표를 달성한 반다라트가 존재하는 경우
        if (completedKeys.isNotEmpty()) {
            return getBandalart(currentState, completedKeys[0], isBandalartCompleted = true)
        }

        // 데이터를 동기화
        bandalartList.forEach { bandalart ->
            bandalartRepository.upsertBandalartId(bandalart.id, bandalart.isCompleted)
        }

        // 반다라트 목록이 존재하지 않을 경우
        if (bandalartList.isEmpty()) {
            bandalartRepository.createBandalart()?.let { bandalart ->
                return getBandalart(currentState, bandalart.id)
            }
        }

        // 반다라트 목록이 존재할 경우
        val recentBandalartId = bandalartRepository.getRecentBandalartId()
        return if (bandalartList.any { it.id == recentBandalartId }) {
            getBandalart(currentState, recentBandalartId)
        } else {
            getBandalart(currentState, bandalartList[0].id)
        }
    }

    private suspend fun checkCompletedBandalart(
        currentState: HomeUiState,
        bandalartId: Long
    ): HomeUiState {
        val isBandalartCompleted = bandalartRepository.checkCompletedBandalartId(bandalartId)
        if (isBandalartCompleted) {
            delay(500L)
            val newState = currentState.copy(isCaptured = true)
            delay(500L)
            return newState
        }
        return currentState
    }

    private suspend fun getBandalart(
        currentState: State,
        bandalartId: Long,
        isBandalartCompleted: Boolean = false
    ): State {
        val bandalart = bandalartRepository.getBandalart(bandalartId)
        val mainCell = bandalartRepository.getBandalartMainCell(bandalartId)
        val subCells = bandalartRepository.getChildCells(mainCell?.id ?: 0L)

        val children = subCells.map { subCell ->
            val taskCells = bandalartRepository.getChildCells(subCell.id)
            BandalartCellEntity(
                id = subCell.id,
                title = subCell.title,
                description = subCell.description,
                dueDate = subCell.dueDate,
                isCompleted = subCell.isCompleted,
                parentId = subCell.parentId,
                children = taskCells.map { taskCell ->
                    BandalartCellEntity(
                        id = taskCell.id,
                        title = taskCell.title,
                        description = taskCell.description,
                        dueDate = taskCell.dueDate,
                        isCompleted = taskCell.isCompleted,
                        parentId = taskCell.parentId,
                        children = emptyList(),
                    )
                },
            )
        }

        return currentState.copy(
            bandalartData = bandalart.toUiModel(),
            isBandalartListBottomSheetOpened = false,
            isBandalartCompleted = isBandalartCompleted,
            bandalartCellData = BandalartCellEntity(
                id = mainCell?.id ?: 0L,
                title = mainCell?.title,
                description = mainCell?.description,
                dueDate = mainCell?.dueDate,
                isCompleted = mainCell?.isCompleted ?: false,
                parentId = mainCell?.parentId,
                children = children,
            )
        )
    }

    @CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }
}
