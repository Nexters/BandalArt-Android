package com.nexters.bandalart.feature.home.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.core.common.utils.UiText
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.feature.home.mapper.toUiModel
import com.nexters.bandalart.feature.home.model.CellType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

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
        updateSkeletonState(true)
        getBandalartList()
        observeBandalartCompletion()
    }

    private fun observeBandalartCompletion() {
        viewModelScope.launch {
            bandalartFlow.collect { bandalart ->
                if (bandalart.isCompleted && !bandalart.title.isNullOrEmpty()) {
                    val isBandalartCompleted = checkCompletedBandalartId(bandalart.id)
                    if (isBandalartCompleted) {
                        delay(500L)
                        updateCaptureState(true)
                        delay(500L)
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
            is HomeUiAction.OnListClick -> toggleBandalartListBottomSheet(true)
            is HomeUiAction.OnSaveClick -> updateCaptureState(true)
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
                    ModalType.DELETE_DIALOG -> _uiState.value.bandalartData?.let { deleteBandalart(it.id) }
                    else -> {}
                }
            }

            is HomeUiAction.OnCancelClick -> {
                when (action.modalType) {
                    ModalType.EMOJI -> toggleEmojiBottomSheet(false)
                    ModalType.BANDALART_LIST -> toggleBandalartListBottomSheet(false)
                    ModalType.DELETE_DIALOG -> toggleBandalartDeleteAlertDialog(false)
                    else -> {}
                }
            }

            is HomeUiAction.OnShareButtonClick -> updateShareState()
            is HomeUiAction.OnAddClick -> createBandalart()
            is HomeUiAction.ToggleDropDownMenu -> toggleDropDownMenu(action.flag)
            is HomeUiAction.ToggleDeleteAlertDialog -> toggleBandalartDeleteAlertDialog(action.flag)
            is HomeUiAction.ToggleEmojiBottomSheet -> toggleEmojiBottomSheet(action.flag)
            is HomeUiAction.ToggleCellBottomSheet -> toggleCellBottomSheet(action.flag)
            is HomeUiAction.ToggleBandalartListBottomSheet -> toggleBandalartListBottomSheet(action.flag)
            is HomeUiAction.OnBandalartListItemClick -> {
                setRecentBandalartId(action.key)
                getBandalart(action.key)
            }

            is HomeUiAction.OnBandalartCellClick -> handleBandalartCellClick(action.cellType, action.isMainCellTitleEmpty, action.cellData)
            is HomeUiAction.OnCloseButtonClick -> toggleCellBottomSheet(false)
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

    private fun getBandalartList() {
        viewModelScope.launch {
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
    }

    private fun getBandalart(bandalartId: Long, isBandalartCompleted: Boolean = false) {
        viewModelScope.launch {
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
    }

    private fun getBandalartMainCell(bandalartId: Long) {
        viewModelScope.launch {
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
    }

    private fun createBandalart() {
        viewModelScope.launch {
            if (_uiState.value.bandalartList.size + 1 > 5) {
                _uiEvent.send(HomeUiEvent.ShowToast(UiText.StringResource(R.string.limit_create_bandalart)))
                return@launch
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
                _uiEvent.send(HomeUiEvent.ShowSnackbar(UiText.StringResource(R.string.create_bandalart)))
            }
        }
    }

    fun saveBandalartImage(bitmap: ImageBitmap) {
        viewModelScope.launch {
            _uiEvent.send(HomeUiEvent.SaveBandalart(bitmap))
            toggleDropDownMenu(false)
        }
    }

    private fun deleteBandalart(bandalartId: Long) {
        viewModelScope.launch {
            updateSkeletonState(true)
            bandalartRepository.deleteBandalart(bandalartId)
            toggleBandalartDeleteAlertDialog(false)
            deleteBandalartId(bandalartId)
            _uiEvent.send(HomeUiEvent.ShowSnackbar(UiText.StringResource(R.string.delete_bandalart)))
        }
    }

    private fun updateBandalartEmoji(
        bandalartId: Long,
        cellId: Long,
        updateBandalartEmojiModel: UpdateBandalartEmojiEntity,
    ) {
        viewModelScope.launch {
            bandalartRepository.updateBandalartEmoji(bandalartId, cellId, updateBandalartEmojiModel)
            toggleEmojiBottomSheet(false)
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

    private fun updateCaptureState(flag: Boolean) {
        _uiState.update { it.copy(isCaptured = flag) }
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

    private fun setRecentBandalartId(bandalartId: Long) {
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

    private fun handleBandalartCellClick(
        cellType: CellType,
        isMainCellTitleEmpty: Boolean,
        cellData: BandalartCellEntity,
    ) {
        when {
            // 메인셀이 비어있고, 서브나 태스크셀 클릭 시
            cellType != CellType.MAIN && isMainCellTitleEmpty -> {
                viewModelScope.launch {
                    _uiEvent.send(HomeUiEvent.ShowToast(UiText.StringResource(R.string.please_input_main_goal)))
                }
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
}
