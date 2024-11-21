package com.nexters.bandalart.feature.home.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.core.common.utils.UiText
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.feature.home.mapper.toEntity
import com.nexters.bandalart.feature.home.mapper.toUiModel
import com.nexters.bandalart.feature.home.model.BandalartCellUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.nexters.bandalart.core.ui.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bandalartRepository: BandalartRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<HomeUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val bandalartFlow = uiState
        .map { it.bandalartData }
        .filterNotNull()
        .distinctUntilChanged()

    init {
        _uiState.update { it.copy(isShowSkeleton = true) }
        observeBandalartCompletion()
    }

    private fun observeBandalartCompletion() {
        viewModelScope.launch {
            bandalartFlow.collect { bandalart ->
                if (bandalart.isCompleted && !bandalart.title.isNullOrEmpty()) {
                    val isBandalartCompleted = checkCompletedBandalartId(bandalart.id)
                    if (isBandalartCompleted) {
                        updateCaptureState()
                        delay(1000L)
                        navigateToComplete(
                            bandalart.id,
                            bandalart.title,
                            bandalart.profileEmoji.orEmpty(),
                            _uiState.value.bandalartChartUrl.orEmpty(),
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.OnCreateClick -> createBandalart()
            is HomeUiAction.OnListClick -> toggleBandalartListBottomSheet(true)
            is HomeUiAction.OnDeleteClick -> {
                toggleBandalartDeleteAlertDialog(true)
                toggleDropDownMenu(false)
            }

            is HomeUiAction.OnEmojiSelected -> updateBandalartEmoji(
                action.bandalartId,
                action.cellId,
                action.updateBandalartEmojiModel,
            )

            is HomeUiAction.OnConfirmClick -> {
                when (action.modalType) {
                    ModalType.CELL -> {}
                    ModalType.DELETE_DIALOG -> _uiState.value.bandalartData?.let { deleteBandalart(it.id) }
                    else -> {}
                }
            }

            is HomeUiAction.OnCancelClick -> {
                when (action.modalType) {
                    ModalType.CELL -> toggleCellBottomSheet(false)
                    ModalType.EMOJI -> toggleEmojiBottomSheet(false)
                    ModalType.BANDALART_LIST -> toggleBandalartListBottomSheet(false)
                    ModalType.DELETE_DIALOG -> toggleBandalartDeleteAlertDialog(false)
                }
            }

            is HomeUiAction.OnShareButtonClick -> updateShareState()
            is HomeUiAction.OnAddClick -> createBandalart()
            is HomeUiAction.ToggleDropDownMenu -> toggleDropDownMenu(action.flag)
            is HomeUiAction.ToggleDeleteAlertDialog -> toggleBandalartDeleteAlertDialog(action.flag)
            is HomeUiAction.ToggleEmojiBottomSheet -> toggleEmojiBottomSheet(action.flag)
            is HomeUiAction.ToggleCellBottomSheet -> toggleCellBottomSheet(action.flag)
            is HomeUiAction.BottomSheetDataChanged -> updateBottomSheetData(true)
            is HomeUiAction.ShowSkeletonChanged -> updateSkeletonState(true)
            is HomeUiAction.ToggleBandalartListBottomSheet -> toggleBandalartListBottomSheet(action.flag)
        }
    }

    private fun navigateToComplete(bandalartId: Long, title: String, profileEmoji: String, bandalartChart: String) {
        viewModelScope.launch {
            _uiEvent.send(
                HomeUiEvent.NavigateToComplete(
                    id = bandalartId,
                    title = title,
                    profileEmoji = profileEmoji,
                    bandalartChart = bandalartChart,
                ),
            )
        }
    }

    fun getBandalartList(bandalartId: Long? = null) {
        viewModelScope.launch {
            val bandalartList = bandalartRepository.getBandalartList().map { it.toUiModel() }
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
                return@launch
            }

            // 서버의 데이터와 로컬의 데이터를 동기화
            bandalartList.forEach { bandalart ->
                bandalartRepository.upsertBandalartId(bandalart.id, bandalart.isCompleted)
            }

            // 생성한 반다라트 표를 화면에 띄우는 경우
            if (bandalartId != null) {
                _uiState.update { it.copy(isShowSkeleton = true) }
                getBandalart(bandalartId)
                return@launch
            }
            // 반다라트 목록이 존재하지 않을 경우, 새로운 반다라트를 생성
            if (bandalartList.isEmpty()) {
                createBandalart()
                return@launch
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
                    _uiState.update { it.copy(isShowSkeleton = true) }
                    // 목록에 가장 첫번째 표를 화면에 띄움
                    getBandalart(bandalartList[0].id)
                }
            }
        }
    }

    fun getBandalart(bandalartId: Long, isBandalartCompleted: Boolean = false) {
        viewModelScope.launch {
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
        }
        _uiState.update { it.copy(isShowSkeleton = false) }
    }

    private fun getBandalartMainCell(bandalartId: Long) {
        viewModelScope.launch {
            val mainCell = bandalartRepository.getBandalartMainCell(bandalartId)
            val subCells = bandalartRepository.getChildCells(mainCell?.id ?: 0L)

            val children = subCells.map { subCell ->
                val taskCells = bandalartRepository.getChildCells(subCell.id)
                BandalartCellUiModel(
                    id = subCell.id,
                    title = subCell.title,
                    description = subCell.description,
                    dueDate = subCell.dueDate,
                    isCompleted = subCell.isCompleted,
                    parentId = subCell.parentId,
                    children = taskCells.map { taskCell ->
                        BandalartCellUiModel(
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
                    bandalartCellData = BandalartCellUiModel(
                        id = mainCell?.id ?: 0L,
                        title = mainCell?.title,
                        description = mainCell?.description,
                        dueDate = mainCell?.dueDate,
                        isCompleted = mainCell?.isCompleted ?: false,
                        parentId = mainCell?.parentId,
                        children = children,
                    ),
                    isShowSkeleton = false,
                )
            }
            updateBottomSheetData(flag = false)
        }
    }

    private fun createBandalart() {
        viewModelScope.launch {
            if (_uiState.value.bandalartList.size + 1 > 5) {
                _uiEvent.send(HomeUiEvent.ShowToast(UiText.StringResource(R.string.limit_create_bandalart)))
                return@launch
            }

            _uiState.update { it.copy(isShowSkeleton = true) }

            bandalartRepository.createBandalart()?.let { bandalart ->
                _uiState.update {
                    it.copy(isBandalartListBottomSheetOpened = false)
                }
                // 새로운 반다라트를 생성하면 화면에 생성된 반다라트 표를 보여주도록 id 를 전달
                getBandalartList(bandalart.id)
                // 새로운 반다라트의 키를 최근에 확인한 반다라트로 저장
                setRecentBandalartId(bandalart.id)
                // 새로운 반다라트를 로컬에 저장
                upsertBandalartId(bandalart.id)
                _uiEvent.send(HomeUiEvent.ShowSnackbar(UiText.StringResource(R.string.create_bandalart)))
            }
        }
    }

    private fun deleteBandalart(bandalartId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isShowSkeleton = true) }

            bandalartRepository.deleteBandalart(bandalartId)
            _uiState.update {
                it.copy(isBandalartDeleted = true)
            }
            toggleBandalartDeleteAlertDialog(false)
            getBandalartList()
            deleteBandalartId(bandalartId)
            _uiEvent.send(HomeUiEvent.ShowSnackbar(UiText.StringResource(R.string.delete_bandalart)))
        }
    }

    private fun updateBandalartEmoji(
        bandalartId: Long,
        cellId: Long,
        updateBandalartEmojiModel: com.nexters.bandalart.feature.home.model.UpdateBandalartEmojiModel,
    ) {
        viewModelScope.launch {
            bandalartRepository.updateBandalartEmoji(bandalartId, cellId, updateBandalartEmojiModel.toEntity())
        }
    }

    private fun updateShareState() {
        _uiState.update { it.copy(isShared = true) }
    }

    fun shareBandalart(bitmap: ImageBitmap) {
        viewModelScope.launch {
            _uiState.update { it.copy(isShared = false) }
            _uiEvent.send(HomeUiEvent.ShareBandalart(bitmap))
        }
    }

    private fun updateCaptureState() {
        _uiState.update { it.copy(isCaptured = true) }
    }

    fun captureBandalart(bitmap: ImageBitmap) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCaptured = false) }
            _uiEvent.send(HomeUiEvent.CaptureBandalart(bitmap))
        }
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

    private fun toggleCellBottomSheet(flag: Boolean) {
        _uiState.update { it.copy(isCellBottomSheetOpened = flag) }
    }

    fun updateBottomSheetData(flag: Boolean) {
        _uiState.update { it.copy(isBottomSheetDataChanged = flag) }
    }

    fun updateSkeletonState(flag: Boolean) {
        _uiState.update { it.copy(isShowSkeleton = flag) }
    }

    private fun toggleBandalartListBottomSheet(flag: Boolean) {
        _uiState.update { it.copy(isBandalartListBottomSheetOpened = flag) }
    }

    private suspend fun getRecentBandalartId(): Long {
        return bandalartRepository.getRecentBandalartId()
    }

    fun setRecentBandalartId(bandalartId: Long) {
        viewModelScope.launch {
            bandalartRepository.setRecentBandalartId(bandalartId)
        }
    }

    private fun upsertBandalartId(bandalartId: Long, isCompleted: Boolean = false) {
        viewModelScope.launch {
            bandalartRepository.upsertBandalartId(bandalartId, isCompleted)
        }
    }

    private suspend fun checkCompletedBandalartId(bandalartId: Long): Boolean {
        return withContext(viewModelScope.coroutineContext) {
            bandalartRepository.checkCompletedBandalartId(bandalartId)
        }
    }

    private fun deleteBandalartId(bandalartId: Long) {
        viewModelScope.launch {
            bandalartRepository.deleteCompletedBandalartId(bandalartId)
        }
    }

    fun updateBandalartChartUrl(url: String) {
        _uiState.update { it.copy(bandalartChartUrl = url) }
    }
}
