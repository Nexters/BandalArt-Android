package com.nexters.bandalart.feature.home.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.core.common.utils.UiText
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartMainCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartSubCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartTaskCellEntity
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.core.domain.repository.InAppUpdateRepository
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.checkState
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
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bandalartRepository: BandalartRepository,
    private val inAppUpdateRepository: InAppUpdateRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Content())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<HomeUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val bandalartFlow = uiState
        .filterIsInstance<HomeUiState.Content>()
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
                        updateCaptureState()
                        delay(500L)
                        val chartUrl = (_uiState.value as? HomeUiState.Content)?.bandalartChartUrl.orEmpty()
                        navigateToComplete(
                            bandalart.id,
                            bandalart.title,
                            bandalart.profileEmoji.orEmpty(),
                            chartUrl,
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.OnListClick -> {
                uiState.checkState<HomeUiState.Content> {
                    _uiState.update {
                        copy(
                            modal = ModalState.Modals(
                                bottomSheet = BottomSheetModal.BandalartList(
                                    BottomSheetData(),
                                ),
                            ),
                        )
                    }
                }
            }

            is HomeUiAction.OnSaveClick -> updateCaptureState()
            is HomeUiAction.OnDeleteClick -> {
                uiState.checkState<HomeUiState.Content> {
                    _uiState.update {
                        copy(
                            modal = ModalState.Modals(
                                dialog = DialogModal.Delete,
                            ),
                        )
                    }
                }
            }

            is HomeUiAction.OnEmojiSelected -> updateBandalartEmoji(
                action.bandalartId,
                action.cellId,
                action.updateBandalartEmojiModel,
            )

            is HomeUiAction.OnConfirmClick -> {
                when (action.modalType) {
                    ModalType.DELETE_DIALOG -> {
                        // TODO 이런 로직도 checkState 함수를 살짝 바꿔서 제거할 수 있을 것 같음
                        if (_uiState.value is HomeUiState.Content) {
                            (_uiState.value as HomeUiState.Content).bandalartData?.let {
                                deleteBandalart(it.id)
                            }
                        }
                    }

                    else -> {}
                }
            }

            is HomeUiAction.OnCancelClick -> hideModal()
            is HomeUiAction.OnShareButtonClick -> updateShareState()
            is HomeUiAction.OnAddClick -> createBandalart()
            is HomeUiAction.ToggleDropDownMenu -> {
                uiState.checkState<HomeUiState.Content> {
                    _uiState.update {
                        copy(modal = if (action.flag) ModalState.DropDownMenu else ModalState.Hidden)
                    }
                }
            }

            is HomeUiAction.ToggleDeleteAlertDialog -> {
                uiState.checkState<HomeUiState.Content> {
                    _uiState.update {
                        copy(
                            modal = if (action.flag) {
                                ModalState.Modals(dialog = DialogModal.Delete)
                            } else ModalState.Hidden,
                        )
                    }
                }
            }

            is HomeUiAction.ToggleEmojiBottomSheet -> {
                uiState.checkState<HomeUiState.Content> {
                    _uiState.update {
                        copy(
                            modal = if (action.flag) {
                                ModalState.Modals(bottomSheet = BottomSheetModal.Emoji(BottomSheetData()))
                            } else ModalState.Hidden,
                        )
                    }
                }
            }

            is HomeUiAction.ToggleCellBottomSheet -> {
                if (!action.flag) {
                    hideModal()
                } else {
                    val currentState = _uiState.value
                    if (currentState is HomeUiState.Content) {
                        handleBandalartCellClick(
                            currentState.clickedCellType,
                            currentState.clickedCellData?.title.isNullOrEmpty(),
                            currentState.clickedCellData ?: return,
                        )
                    }
                }
            }

            is HomeUiAction.ToggleBandalartListBottomSheet -> {
                uiState.checkState<HomeUiState.Content> {
                    _uiState.update {
                        copy(
                            modal = if (action.flag) {
                                ModalState.Modals(bottomSheet = BottomSheetModal.BandalartList(BottomSheetData()))
                            } else ModalState.Hidden,
                        )
                    }
                }
            }

            is HomeUiAction.OnBandalartListItemClick -> {
                setRecentBandalartId(action.key)
                getBandalart(action.key)
                hideModal()
            }

            is HomeUiAction.OnBandalartCellClick -> handleBandalartCellClick(
                action.cellType,
                action.isMainCellTitleEmpty,
                action.cellData,
            )

            is HomeUiAction.OnCloseButtonClick -> hideModal()
            is HomeUiAction.OnAppTitleClick -> showAppVersion()
            is HomeUiAction.OnCellTitleUpdate -> updateCellTitle(action.title, action.locale)
            is HomeUiAction.OnEmojiSelect -> updateEmoji(action.emoji)
            is HomeUiAction.OnColorSelect -> updateThemeColor(action.mainColor, action.subColor)
            is HomeUiAction.OnDueDateSelect -> updateDueDate(action.date)
            is HomeUiAction.OnDescriptionUpdate -> updateDescription(action.description)
            is HomeUiAction.OnCompletionUpdate -> updateCompletion(action.isCompleted)
            is HomeUiAction.OnDeleteCell -> deleteCell(action.cellId)
            is HomeUiAction.OnCancelDeleteCell -> {
                uiState.checkState<HomeUiState.Content> {
                    _uiState.update {
                        copy(modal = (modal as? ModalState.Modals)?.copy(dialog = null) ?: ModalState.Hidden)
                    }
                }
            }

            is HomeUiAction.OnEmojiPickerClick -> {
                uiState.checkState<HomeUiState.Content> {
                    val currentBottomSheet = (modal as? ModalState.Modals)?.bottomSheet as? BottomSheetModal.Cell
                    copy(
                        modal = ModalState.Modals(
                            bottomSheet = currentBottomSheet?.copy(
                                data = currentBottomSheet.data.copy(
                                    isEmojiPickerOpened = true,
                                ),
                            ),
                        ),
                    )
                }
            }

            is HomeUiAction.OnDatePickerClick -> {
                uiState.checkState<HomeUiState.Content> {
                    val currentBottomSheet = (modal as? ModalState.Modals)?.bottomSheet as? BottomSheetModal.Cell
                    copy(
                        modal = ModalState.Modals(
                            bottomSheet = currentBottomSheet?.copy(
                                data = currentBottomSheet.data.copy(
                                    isDatePickerOpened = true,
                                ),
                            ),
                        ),
                    )
                }
            }

            is HomeUiAction.OnCloseBottomSheet -> hideModal()

            is HomeUiAction.OnDeleteButtonClick -> {
                uiState.checkState<HomeUiState.Content> {
                    _uiState.update {
                        copy(
                            modal = ModalState.Modals(
                                dialog = DialogModal.Delete,
                            ),
                        )
                    }
                }
            }

            is HomeUiAction.OnCompleteButtonClick -> {
                updateCell(
                    bandalartId = action.bandalartId,
                    cellId = action.cellId,
                    cellType = action.cellType,
                )
            }
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
                    uiState.checkState<HomeUiState.Content> {
                        _uiState.update { copy(bandalartList = bandalartList.toImmutableList()) }
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
                uiState.checkState<HomeUiState.Content> {
                    _uiState.update {
                        copy(
                            bandalartData = bandalart.toUiModel(),
                            modal = ModalState.Hidden,
                            isBandalartCompleted = isBandalartCompleted,
                        )
                    }
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

            uiState.checkState<HomeUiState.Content> {
                _uiState.update {
                    copy(
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
            }
            updateSkeletonState(false)
        }
    }

    private fun createBandalart() {
        viewModelScope.launch {
            if (_uiState.value is HomeUiState.Content && (_uiState.value as HomeUiState.Content).bandalartList.size + 1 > 5) {
                _uiEvent.send(HomeUiEvent.ShowToast(UiText.StringResource(R.string.limit_create_bandalart)))
                return@launch
            }

            updateSkeletonState(true)
            bandalartRepository.createBandalart()?.let { bandalart ->
                hideModal()
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
            hideModal()
        }
    }

    private fun deleteBandalart(bandalartId: Long) {
        viewModelScope.launch {
            updateSkeletonState(true)
            bandalartRepository.deleteBandalart(bandalartId)
            hideModal()
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
            uiState.checkState<HomeUiState.Content> {
                _uiState.update { copy(modal = (modal as? ModalState.Modals)?.copy(bottomSheet = null) ?: ModalState.Hidden) }
            }
        }
    }

    private fun updateShareState() {
        uiState.checkState<HomeUiState.Content> {
            _uiState.update { copy(shareState = ShareState.Share) }
        }
    }

    fun shareBandalart(bitmap: ImageBitmap) {
        viewModelScope.launch {
            uiState.checkState<HomeUiState.Content> {
                _uiState.update { copy(shareState = ShareState.None) }
            }
            _uiEvent.send(HomeUiEvent.ShareBandalart(bitmap))
        }
    }

    private fun updateCaptureState() {
        uiState.checkState<HomeUiState.Content> {
            _uiState.update { copy(shareState = ShareState.Capture) }
        }
    }

    fun captureBandalart(bitmap: ImageBitmap) {
        viewModelScope.launch {
            uiState.checkState<HomeUiState.Content> {
                _uiState.update { copy(shareState = ShareState.None) }
            }
            _uiEvent.send(HomeUiEvent.CaptureBandalart(bitmap))
        }
    }

    private fun updateSkeletonState(flag: Boolean) {
        uiState.checkState<HomeUiState.Content> {
            _uiState.update { copy(isShowSkeleton = flag) }
        }
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
        uiState.checkState<HomeUiState.Content> {
            _uiState.update { copy(bandalartChartUrl = url) }
        }
    }

    // TODO 태스크셀이고 상위 서브셀이 비어있을 때(테스트셀이 자신의 부모를 알고있어야 구현 가능 함)
    private fun handleBandalartCellClick(
        cellType: CellType,
        isMainCellTitleEmpty: Boolean,
        cellData: BandalartCellEntity,
    ) {
        when {
            cellType != CellType.MAIN && isMainCellTitleEmpty -> {
                viewModelScope.launch {
                    _uiEvent.send(HomeUiEvent.ShowToast(UiText.StringResource(R.string.please_input_main_goal)))
                }
            }

            else -> {
                uiState.checkState<HomeUiState.Content> {
                    _uiState.update {
                        copy(
                            clickedCellData = cellData,
                            clickedCellType = cellType,
                            modal = ModalState.Modals(
                                bottomSheet = BottomSheetModal.Cell(
                                    data = BottomSheetData(
                                        cellData = cellData,
                                    ),
                                ),
                            ),
                        )
                    }
                }
            }
        }
    }

    private fun updateCellTitle(title: String, locale: Locale) {
        val maxLength = when (locale.language) {
            Locale.KOREAN.language, Locale.JAPAN.language -> 15
            else -> 24
        }

        uiState.checkState<HomeUiState.Content> {
            _uiState.update {
                val currentBottomSheet = (modal as? ModalState.Modals)?.bottomSheet as? BottomSheetModal.Cell
                val validatedTitle = if (title.length > maxLength) {
                    currentBottomSheet?.data?.cellData?.title ?: ""
                } else title

                copy(
                    modal = ModalState.Modals(
                        bottomSheet = currentBottomSheet?.copy(
                            data = currentBottomSheet.data.copy(
                                cellData = currentBottomSheet.data.cellData.copy(
                                    title = validatedTitle,
                                ),
                            ),
                        ),
                    ),
                )
            }
        }
    }

    private fun updateEmoji(emoji: String) {
        uiState.checkState<HomeUiState.Content> {
            _uiState.update {
                val currentBottomSheet = (modal as? ModalState.Modals)?.bottomSheet as? BottomSheetModal.Cell
                copy(
                    modal = ModalState.Modals(
                        bottomSheet = currentBottomSheet?.copy(
                            data = currentBottomSheet.data.copy(
                                bandalartData = currentBottomSheet.data.bandalartData.copy(profileEmoji = emoji),
                                isEmojiPickerOpened = false,
                            ),
                        ),
                    ),
                )
            }
        }
    }

    private fun updateThemeColor(mainColor: String, subColor: String) {
        uiState.checkState<HomeUiState.Content> {
            _uiState.update {
                val currentBottomSheet = (modal as? ModalState.Modals)?.bottomSheet as? BottomSheetModal.Cell
                copy(
                    modal = ModalState.Modals(
                        bottomSheet = currentBottomSheet?.copy(
                            data = currentBottomSheet.data.copy(
                                bandalartData = currentBottomSheet.data.bandalartData.copy(
                                    mainColor = mainColor,
                                    subColor = subColor,
                                ),
                            ),
                        ),
                    ),
                )
            }
        }
    }

    private fun updateDueDate(date: String) {
        uiState.checkState<HomeUiState.Content> {
            _uiState.update {
                val currentBottomSheet = (modal as? ModalState.Modals)?.bottomSheet as? BottomSheetModal.Cell
                copy(
                    modal = ModalState.Modals(
                        bottomSheet = currentBottomSheet?.copy(
                            data = currentBottomSheet.data.copy(
                                cellData = currentBottomSheet.data.cellData.copy(dueDate = date),
                                isDatePickerOpened = false,
                            ),
                        ),
                    ),
                )
            }
        }
    }

    private fun updateDescription(description: String) {
        uiState.checkState<HomeUiState.Content> {
            _uiState.update {
                val currentBottomSheet = (modal as? ModalState.Modals)?.bottomSheet as? BottomSheetModal.Cell
                val validatedDescription = if ((description.length) > 1000) {
                    currentBottomSheet?.data?.cellData?.description
                } else description

                copy(
                    modal = ModalState.Modals(
                        bottomSheet = currentBottomSheet?.copy(
                            data = currentBottomSheet.data.copy(
                                cellData = currentBottomSheet.data.cellData.copy(
                                    description = validatedDescription,
                                ),
                            ),
                        ),
                    ),
                )
            }
        }
    }

    private fun updateCompletion(isCompleted: Boolean) {
        uiState.checkState<HomeUiState.Content> {
            _uiState.update {
                val currentBottomSheet = (modal as? ModalState.Modals)?.bottomSheet as? BottomSheetModal.Cell
                copy(
                    modal = ModalState.Modals(
                        bottomSheet = currentBottomSheet?.copy(
                            data = currentBottomSheet.data.copy(
                                cellData = currentBottomSheet.data.cellData.copy(isCompleted = isCompleted),
                            ),
                        ),
                    ),
                )
            }
        }
    }

    private fun deleteCell(cellId: Long) {
        viewModelScope.launch {
            bandalartRepository.deleteBandalartCell(cellId)
            hideModal()
        }
    }

    private fun hideModal() {
        uiState.checkState<HomeUiState.Content> {
            _uiState.update { copy(modal = ModalState.Hidden) }
        }
    }

    private fun updateCell(
        bandalartId: Long,
        cellId: Long,
        cellType: CellType,
    ) {
        val currentState = _uiState.value
        if (currentState !is HomeUiState.Content) return

        val bottomSheetData = (currentState.modal as? ModalState.Modals)?.bottomSheet as? BottomSheetModal.Cell ?: return
        val cellData = bottomSheetData.data.cellData
        val bandalartData = bottomSheetData.data.bandalartData

        val trimmedTitle = cellData.title?.trim()
        val description = cellData.description
        val dueDate = cellData.dueDate?.ifEmpty { null }

        when (cellType) {
            CellType.MAIN -> {
                updateMainCell(
                    bandalartId = bandalartId,
                    cellId = cellId,
                    updateBandalartMainCellModel = UpdateBandalartMainCellEntity(
                        title = trimmedTitle,
                        description = description,
                        dueDate = dueDate,
                        profileEmoji = bandalartData.profileEmoji,
                        mainColor = bandalartData.mainColor,
                        subColor = bandalartData.subColor,
                    ),
                )
            }

            CellType.SUB -> {
                updateSubCell(
                    bandalartId = bandalartId,
                    cellId = cellId,
                    updateBandalartSubCellModel = UpdateBandalartSubCellEntity(
                        title = trimmedTitle,
                        description = description,
                        dueDate = dueDate,
                    ),
                )
            }

            else -> {
                updateTaskCell(
                    bandalartId = bandalartId,
                    cellId = cellId,
                    updateBandalartTaskCellModel = UpdateBandalartTaskCellEntity(
                        title = trimmedTitle,
                        description = description,
                        dueDate = dueDate,
                        isCompleted = cellData.isCompleted,
                    ),
                )
            }
        }
    }

    private fun updateMainCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartMainCellModel: UpdateBandalartMainCellEntity,
    ) {
        viewModelScope.launch {
            bandalartRepository.updateBandalartMainCell(bandalartId, cellId, updateBandalartMainCellModel)
            bandalartRepository.getBandalart(bandalartId)
            hideModal()
        }
    }

    private fun updateSubCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartSubCellModel: UpdateBandalartSubCellEntity,
    ) {
        viewModelScope.launch {
            bandalartRepository.updateBandalartSubCell(bandalartId, cellId, updateBandalartSubCellModel)
            hideModal()
        }
    }

    private fun updateTaskCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartTaskCellModel: UpdateBandalartTaskCellEntity,
    ) {
        viewModelScope.launch {
            bandalartRepository.updateBandalartTaskCell(bandalartId, cellId, updateBandalartTaskCellModel)
            hideModal()
        }
    }

    private fun showAppVersion() {
        viewModelScope.launch {
            _uiEvent.send(HomeUiEvent.ShowAppVersion)
        }
    }

    suspend fun setLastRejectedUpdateVersion(versionCode: Int) {
        inAppUpdateRepository.setLastRejectedUpdateVersion(versionCode)
    }

    suspend fun isUpdateAlreadyRejected(versionCode: Int): Boolean {
        return inAppUpdateRepository.isUpdateAlreadyRejected(versionCode)
    }
}
