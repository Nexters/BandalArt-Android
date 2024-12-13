package com.nexters.bandalart.feature.home.presenter

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import com.nexters.bandalart.core.common.extension.bitmapToFileUri
import com.nexters.bandalart.core.common.extension.externalShareForBitmap
import com.nexters.bandalart.core.common.extension.saveImageToGallery
import com.nexters.bandalart.core.common.utils.UiText
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.core.domain.repository.InAppUpdateRepository
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.complete.CompleteScreen
import com.nexters.bandalart.feature.home.HomeScreen
import com.nexters.bandalart.feature.home.HomeScreen.Event
import com.nexters.bandalart.feature.home.HomeScreen.State
import com.nexters.bandalart.feature.home.mapper.toUiModel
import com.nexters.bandalart.feature.home.model.BandalartUiModel
import com.nexters.bandalart.feature.home.model.CellType
import com.nexters.bandalart.feature.home.viewmodel.HomeEvent
import com.nexters.bandalart.feature.home.viewmodel.HomeUiState
import com.nexters.bandalart.feature.home.viewmodel.ModalType
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
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
) : Presenter<HomePresenter.State> {

    data class State(
        val uiState: HomeUiState = HomeUiState(),
        val eventSink: (HomeEvent) -> Unit,
    ): CircuitUiState

    @Composable
    override fun present(): State {
        // TODO rememberCoroutineScope 와 뭔 차이인지 확인
        // val scope = rememberCoroutineScope()
        val scope = rememberStableCoroutineScope()

        var uiState by rememberRetained { mutableStateOf(HomeUiState()) }
        val eventChannel = rememberRetained { Channel<HomeEvent>() }

        LaunchedEffect(Unit) {
            bandalartRepository.getBandalartList()
                .map { list -> list.map { it.toUiModel() } }
                .filterNotNull()
                .distinctUntilChanged()
                .collect { bandalartList ->
                    uiState = uiState.copy(bandalartList = bandalartList.toImmutableList())
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

        // 초기화
        LaunchedEffect(Unit) {
            updateSkeletonState(true)
            getBandalartList()
            observeBandalartCompletion()
        }

        // 이벤트 처리
        LaunchedEffect(Unit) {
            eventChannel.receiveAsFlow().collect { event ->
                when (event) {
                    is HomeEvent.NavigateToComplete -> {
                        navigator.goTo(CompleteScreen(
                            bandalartId = event.id,
                            bandalartTitle = event.title,
                            bandalartProfileEmoji = event.profileEmoji,
                            bandalartChartImageUri = event.bandalartChart
                        ))
                    }
                    is HomeEvent.ShowSnackbar -> {
                        // Snackbar 표시 로직
                    }
                    is HomeEvent.ShowToast -> {
                        // Toast 표시 로직
                    }
                    is HomeEvent.SaveBandalart -> {
                        saveBandalartImage(event.bitmap)
                    }
                    is HomeEvent.ShareBandalart -> {
                        shareBandalart(event.bitmap)
                    }
                    is HomeEvent.CaptureBandalart -> {
                        captureBandalart(event.bitmap)
                    }
                    is HomeEvent.ShowAppVersion -> {
                        // 앱 버전 표시 로직
                    }
                }
            }
        }

        // 이벤트 싱크
        val eventSink: (HomeEvent) -> Unit = { event ->
            scope.launch {
                when (event) {
                    is HomeEvent.OnListClick -> {
                        toggleBandalartListBottomSheet(true)
                    }

                    is HomeEvent.OnSaveClick -> {
                        updateCaptureState(true)
                    }

                    is HomeEvent.OnDeleteClick -> {
                        toggleBandalartDeleteAlertDialog(true)
                        toggleDropDownMenu(false)
                    }

                    is HomeEvent.OnEmojiSelected -> {
                        updateBandalartEmoji(
                            event.bandalartId,
                            event.cellId,
                            event.updateBandalartEmojiModel
                        )
                    }

                    is HomeEvent.OnConfirmClick -> {
                        when (event.modalType) {
                            ModalType.DELETE_DIALOG -> uiState.bandalartData?.let {
                                deleteBandalart(it.id)
                            }

                            else -> {}
                        }
                    }

                    is HomeEvent.OnCancelClick -> {
                        when (event.modalType) {
                            ModalType.EMOJI -> toggleEmojiBottomSheet(false)
                            ModalType.BANDALART_LIST -> toggleBandalartListBottomSheet(false)
                            ModalType.DELETE_DIALOG -> toggleBandalartDeleteAlertDialog(false)
                            else -> {}
                        }
                    }

                    is HomeEvent.OnShareButtonClick -> {
                        updateShareState()
                    }

                    is HomeEvent.OnAddClick -> {
                        createBandalart()
                    }

                    is HomeEvent.ToggleDropDownMenu -> {
                        toggleDropDownMenu(event.flag)
                    }

                    is HomeEvent.ToggleDeleteAlertDialog -> {
                        toggleBandalartDeleteAlertDialog(event.flag)
                    }

                    is HomeEvent.ToggleEmojiBottomSheet -> {
                        toggleEmojiBottomSheet(event.flag)
                    }

                    is HomeEvent.ToggleCellBottomSheet -> {
                        toggleCellBottomSheet(event.flag)
                    }

                    is HomeEvent.ToggleBandalartListBottomSheet -> {
                        toggleBandalartListBottomSheet(event.flag)
                    }

                    is HomeEvent.OnBandalartListItemClick -> {
                        setRecentBandalartId(event.key)
                        getBandalart(event.key)
                        toggleBandalartListBottomSheet(false)
                    }

                    is HomeEvent.OnBandalartCellClick -> {
                        handleBandalartCellClick(
                            event.cellType,
                            event.isMainCellTitleEmpty,
                            event.cellData
                        )
                    }

                    is HomeEvent.OnCloseButtonClick -> {
                        toggleCellBottomSheet(false)
                    }

                    is HomeEvent.OnAppTitleClick -> {
                        showAppVersion()
                    }
                }
            }
        }

        return State(uiState, eventSink)

//        return State(
//            bandalartChartUrl = bandalartCaptureUrl,
//            isUpdateAlreadyRejected = isUpdateAlreadyRejected,
//            bandalartData = bandalartData,
//        ) { event ->
//            when (event) {
//                is Event.NavigateToComplete -> navigator.goTo(
//                    CompleteScreen(
//                        bandalartId = event.id,
//                        bandalartTitle = event.title,
//                        bandalartProfileEmoji = event.profileEmoji,
//                        bandalartChartImageUri = event.bandalartChartImageUri,
//                    ),
//                )
//
//                is Event.ShowSnackbar -> {
//                    scope.launch {
//                        snackbarHostState.showSnackbar(
//                            message = event.message.asString(context),
//                            duration = SnackbarDuration.Short,
//                        )
//                    }
//                }
//
//                is Event.ShowToast -> {
//                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
//                }
//
//                is Event.SaveBandalart -> {
//                    scope.launch {
//                        context.saveImageToGallery(event.bitmap)
//                        Toast.makeText(context, context.getString(R.string.save_bandalart_image), Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                is Event.ShareBandalart -> {
//                    context.externalShareForBitmap(event.bitmap)
//                }
//
//                is Event.CaptureBandalart -> {
//                    bandalartCaptureUrl = context.bitmapToFileUri(event.bitmap).toString()
//                }
//
//                is Event.ShowAppVersion -> {
//                    Toast.makeText(context, context.getString(R.string.app_version_info, appVersion), Toast.LENGTH_SHORT).show()
//                }
//
//                is Event.SaveLastRejectedUpdateVersion -> {
//                    scope.launch {
//                        inAppUpdateRepository.setLastRejectedUpdateVersion(event.versionCode)
//                    }
//                }
//                is Event.OnListClick -> toggleBandalartListBottomSheet(true)
//                is Event.OnSaveClick -> updateCaptureState(true)
//                is Event.OnDeleteClick -> {
//                    toggleBandalartDeleteAlertDialog(true)
//                    toggleDropDownMenu(false)
//                }
//
//                is Event.OnEmojiSelected -> {
//                    scope.launch {
//                        updateBandalartEmoji(
//                            event.bandalartId,
//                            event.cellId,
//                            event.updateBandalartEmojiModel,
//                        )
//                    }
//                }
//
//                is Event.OnConfirmClick -> {
//                    when (event.modalType) {
//                        ModalType.DELETE_DIALOG -> bandalartData?.let {
//                            scope.launch {
//                                deleteBandalart(it.id)
//                            }
//                        }
//
//                        else -> {}
//                    }
//                }
//
//                is Event.OnCancelClick -> {
//                    when (event.modalType) {
//                        ModalType.EMOJI -> toggleEmojiBottomSheet(false)
//                        ModalType.BANDALART_LIST -> toggleBandalartListBottomSheet(false)
//                        ModalType.DELETE_DIALOG -> toggleBandalartDeleteAlertDialog(false)
//                        else -> {}
//                    }
//                }
//
//                is Event.OnShareButtonClick -> updateShareState()
//                is Event.OnAddClick -> {
//                    scope.launch {
//                        createBandalart()
//                    }
//                }
//                is Event.ToggleDropDownMenu -> toggleDropDownMenu(event.flag)
//                is Event.ToggleDeleteAlertDialog -> toggleBandalartDeleteAlertDialog(event.flag)
//                is Event.ToggleEmojiBottomSheet -> toggleEmojiBottomSheet(event.flag)
//                is Event.ToggleCellBottomSheet -> toggleCellBottomSheet(event.flag)
//                is Event.ToggleBandalartListBottomSheet -> toggleBandalartListBottomSheet(event.flag)
//                is Event.OnBandalartListItemClick -> {
//                    scope.launch {
//                        setRecentBandalartId(event.key)
//                        getBandalart(event.key)
//                        toggleBandalartListBottomSheet(false)
//                    }
//                }
//
//                is Event.OnBandalartCellClick -> handleBandalartCellClick(event.cellType, event.isMainCellTitleEmpty, event.cellData)
//                is Event.OnCloseButtonClick -> toggleCellBottomSheet(false)
//                is Event.OnAppTitleClick -> showAppVersion(context, appVersion)
//            }
//        }
    }

    private fun navigateToComplete(bandalartId: Long, title: String, profileEmoji: String, bandalartChart: String) {
        _uiEvent.send(
            HomeUiEvent.NavigateToComplete(
                id = bandalartId,
                title = title,
                profileEmoji = profileEmoji,
                bandalartChart = bandalartChart,
            ),
        )
    }

    private suspend fun getBandalartList() {
        bandalartRepository.getBandalartList()
            .map { list -> list.map { it.toUiModel() } }
            .collect { bandalartList ->
                _uiState.update {
                    it.copy(bandalartList = bandalartList.toImmutableList())
                }

                // 이전 반다라트 목록 상태 조회
                val prevBandalartList = bandalartRepository.getPrevBandalartList()

                // 새로 업데이트 된 상태와 이전 상태를 비교
                val completedKeys = bandalartList.filter { bandalart ->
                    val prevBandalart = prevBandalartList.find { it.first == bandalart.id }
                    prevBandalart != null && !prevBandalart.second && bandalart.isCompleted
                }.map { it.id }

                // 이번에 목표를 달성한 반다라트가 존재하는 경우
                if (completedKeys.isNotEmpty()) {
                    getBandalart(completedKeys[0], isBandalartCompleted = true)
                    return@collect
                }

                // 데이터를 동기화
                bandalartList.forEach { bandalart ->
                    bandalartRepository.upsertBandalartId(bandalart.id, bandalart.isCompleted)
                }

                // 반다라트 목록이 존재하지 않을 경우, 새로운 반다라트를 생성
                if (bandalartList.isEmpty()) {
                    createBandalart()
                    return@collect
                } else {
                    // 반다라트 목록이 존재할 경우
                    // 가장 최근에 확인한 반다라트 표를 화면에 띄우는 경우
                    val recentBandalartId = getRecentBandalartId()
                    // 가장 최근에 확인한 반다라트 표가 존재 하는 경우
                    if (bandalartList.any { it.id == recentBandalartId }) {
                        getBandalart(recentBandalartId)
                    }
                    // 가장 최근에 확인한 반다라트 표가 존재 하지 않을 경우
                    else {
                        // 목록에 가장 첫번째 표를 화면에 띄움
                        getBandalart(bandalartList[0].id)
                    }
                }
            }
    }

    private suspend fun getBandalart(bandalartId: Long, isBandalartCompleted: Boolean = false) {
        updateSkeletonState(true)
        bandalartRepository.getBandalart(bandalartId).let { bandalart ->
            _uiState.update {
                it.copy(
                    bandalartData = bandalart.toUiModel(),
                    isBandalartListBottomSheetOpened = false,
                    isBandalartCompleted = isBandalartCompleted,
                )
            }
            getBandalartMainCell(bandalartId)
        }
        updateSkeletonState(false)
    }

    private suspend fun getBandalartMainCell(bandalartId: Long) {
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

        _uiState.update {
            it.copy(
                bandalartCellData = BandalartCellEntity(
                    id = mainCell?.id ?: 0L,
                    title = mainCell?.title,
                    description = mainCell?.description,
                    dueDate = mainCell?.dueDate,
                    isCompleted = mainCell?.isCompleted ?: false,
                    parentId = mainCell?.parentId,
                    children = children,
                ),
            )
        }
        updateSkeletonState(false)
    }

    private suspend fun createBandalart(state: State) {
        if (_uiState.value.bandalartList.size + 1 > 5) {
            _uiEvent.send(HomeUiEvent.ShowToast(UiText.StringResource(R.string.limit_create_bandalart)))
            return
        }

        updateSkeletonState(true)
        bandalartRepository.createBandalart()?.let { bandalart ->
            _uiState.update {
                it.copy(isBandalartListBottomSheetOpened = false)
            }
            // 새로운 반다라트를 생성하면 화면에 생성된 반다라트 표를 보여주도록 id 를 전달
            getBandalart(bandalart.id)
            // 새로운 반다라트의 키를 최근에 확인한 반다라트로 저장
            setRecentBandalartId(bandalart.id)
            // 새로운 반다라트를 로컬에 저장
            upsertBandalartId(bandalart.id)
            // state.eventSink(Event.ShowSnackbar(UiText.StringResource(R.string.create_bandalart)))
            _uiEvent.send(HomeUiEvent.ShowSnackbar(UiText.StringResource(R.string.create_bandalart)))
        }
    }

    fun saveBandalartImage(bitmap: ImageBitmap) {
        _uiEvent.send(HomeUiEvent.SaveBandalart(bitmap))
        toggleDropDownMenu(false)
    }

    private suspend fun deleteBandalart(bandalartId: Long) {
        updateSkeletonState(true)
        bandalartRepository.deleteBandalart(bandalartId)
        toggleBandalartDeleteAlertDialog(false)
        deleteBandalartId(bandalartId)
        _uiEvent.send(HomeUiEvent.ShowSnackbar(UiText.StringResource(R.string.delete_bandalart)))
    }

    private suspend fun updateBandalartEmoji(
        bandalartId: Long,
        cellId: Long,
        updateBandalartEmojiModel: UpdateBandalartEmojiEntity,
    ) {
        bandalartRepository.updateBandalartEmoji(bandalartId, cellId, updateBandalartEmojiModel)
        toggleEmojiBottomSheet(false)
    }

    private fun updateShareState() {
        _uiState.update { it.copy(isShared = true) }
    }

    suspend fun shareBandalart(bitmap: ImageBitmap) {
        _uiState.update { it.copy(isShared = false) }
        _uiEvent.send(HomeUiEvent.ShareBandalart(bitmap))
    }

    private fun updateCaptureState(flag: Boolean) {
        _uiState.update { it.copy(isCaptured = flag) }
    }

    suspend fun captureBandalart(bitmap: ImageBitmap) {
        _uiState.update { it.copy(isCaptured = false) }
        _uiEvent.send(HomeUiEvent.CaptureBandalart(bitmap))
    }

    private fun toggleDropDownMenu(flag: Boolean) {
        _uiState.update { it.copy(isDropDownMenuOpened = flag) }
    }

    private fun toggleBandalartDeleteAlertDialog(flag: Boolean) {
        _uiState.update { it.copy(isBandalartDeleteAlertDialogOpened = flag) }
    }

    private fun toggleEmojiBottomSheet(flag: Boolean) {
        _uiState.update { it.copy(isEmojiBottomSheetOpened = flag) }
    }

    private fun toggleCellBottomSheet(
        flag: Boolean,
        cellType: CellType? = CellType.TASK,
    ) {
        if (!flag) {
            _uiState.update { it.copy(isCellBottomSheetOpened = false) }
        } else {
            when (cellType) {
                CellType.MAIN -> _uiState.update {
                    it.copy(
                        isCellBottomSheetOpened = true,
                        clickedCellType = CellType.MAIN,
                    )
                }

                CellType.SUB -> _uiState.update {
                    it.copy(
                        isCellBottomSheetOpened = true,
                        clickedCellType = CellType.SUB,
                    )
                }

                else -> _uiState.update {
                    it.copy(
                        isCellBottomSheetOpened = true,
                        clickedCellType = CellType.TASK,
                    )
                }
            }
        }
    }

    private fun updateSkeletonState(flag: Boolean) {
        _uiState.update { it.copy(isShowSkeleton = flag) }
    }

    private fun toggleBandalartListBottomSheet(flag: Boolean) {
        _uiState.update { it.copy(isBandalartListBottomSheetOpened = flag) }
    }

    private suspend fun getRecentBandalartId(): Long {
        return bandalartRepository.getRecentBandalartId()
    }

    private suspend fun setRecentBandalartId(bandalartId: Long) {
        bandalartRepository.setRecentBandalartId(bandalartId)
    }

    private suspend fun upsertBandalartId(bandalartId: Long, isCompleted: Boolean = false) {
        bandalartRepository.upsertBandalartId(bandalartId, isCompleted)
    }

    private suspend fun checkCompletedBandalartId(bandalartId: Long): Boolean {
        return bandalartRepository.checkCompletedBandalartId(bandalartId)
    }

    private suspend fun deleteBandalartId(bandalartId: Long) {
        bandalartRepository.deleteCompletedBandalartId(bandalartId)
    }

    fun updateBandalartChartUrl(url: String) {
        _uiState.update { it.copy(bandalartChartUrl = url) }
    }

    private fun handleBandalartCellClick(
        cellType: CellType,
        isMainCellTitleEmpty: Boolean,
        cellData: BandalartCellEntity,
    ) {
        when {
            // 메인셀이 비어있고, 서브나 태스크셀 클릭 시
            cellType != CellType.MAIN && isMainCellTitleEmpty -> {
                _uiEvent.send(HomeUiEvent.ShowToast(UiText.StringResource(R.string.please_input_main_goal)))
            }

            // 태스크셀이고 상위 서브셀이 비어있을 때(테스트셀이 자신의 부모를 알고있어야 구현 가능 함)
//            cellType == CellType.Task && isSubCellTitleEmpty -> {
//                viewModelScope.launch {
//                    _uiEvent.send(HomeUiEvent.ShowToast(UiText.StringResource(R.string.please_input_sub_goal)))
//                }
//            }

            // 그 외의 경우 바텀시트 열기
            else -> {
                _uiState.update { it.copy(clickedCellData = cellData) }
                toggleCellBottomSheet(true, cellType)
            }
        }
    }

    private fun showAppVersion(context: Context, appVersion: String) {
        Toast.makeText(context, context.getString(R.string.app_version_info, appVersion), Toast.LENGTH_SHORT).show()
    }

    suspend fun setLastRejectedUpdateVersion(versionCode: Int) {
        inAppUpdateRepository.setLastRejectedUpdateVersion(versionCode)
    }

    suspend fun isUpdateAlreadyRejected(versionCode: Int): Boolean {
        return inAppUpdateRepository.isUpdateAlreadyRejected(versionCode)
    }

    @CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: HomeScreen,
            navigator: Navigator,
        ): HomePresenter
    }
}
