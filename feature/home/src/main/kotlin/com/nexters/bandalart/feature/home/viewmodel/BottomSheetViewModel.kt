package com.nexters.bandalart.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.feature.home.mapper.toEntity
import com.nexters.bandalart.feature.home.mapper.toUiModel
import com.nexters.bandalart.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.feature.home.model.UpdateBandalartMainCellModel
import com.nexters.bandalart.feature.home.model.UpdateBandalartSubCellModel
import com.nexters.bandalart.feature.home.model.UpdateBandalartTaskCellModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
    private val bandalartRepository: BandalartRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BottomSheetUiState())
    val uiState: StateFlow<BottomSheetUiState> = _uiState.asStateFlow()

//    fun onAction(action: BottomSheetUiAction) {
//        when (action) {
//            is BottomSheetUiAction.CopyCellData -> copyCellData(_uiState.value.cellData)
//            is BottomSheetUiAction.UpdateBandalartMainCell -> updateBandalartMainCell(
//                bandalartId = 0L,
//                cellId = 0L,
//                updateBandalartMainCellModel = UpdateBandalartMainCellModel(
//                    mainColor = "",
//                    subColor = "",
//                ),
//            )
//            is BottomSheetUiAction.UpdateBandalartSubCell -> updateBandalartSubCell(
//                bandalartId = 0L,
//                cellId = 0L,
//                updateBandalartSubCellModel = UpdateBandalartSubCellModel(),
//            )
//            is BottomSheetUiAction.UpdateBandalartTaskCell -> updateBandalartTaskCell(
//                bandalartId = 0L,
//                cellId = 0L,
//                updateBandalartTaskCellModel = UpdateBandalartTaskCellModel(),
//            )
//            is BottomSheetUiAction.DeleteBandalartCell -> deleteBandalartCell(
//                cellId = 0L,
//            )
//            is BottomSheetUiAction.OpenDeleteCellDialog -> toggleDeleteCellDialog(true)
//            is BottomSheetUiAction.OpenDatePicker -> toggleDatePicker(true)
//            is BottomSheetUiAction.OpenEmojiPicker -> toggleEmojiPicker(true)
//            is BottomSheetUiAction.OnModalConfirmClick -> {}
//            is BottomSheetUiAction.TitleChanged -> titleChanged("")
//            is BottomSheetUiAction.OnColorSelect -> colorChanged("", "")
//            is BottomSheetUiAction.OnDueDateChange -> dueDateChanged("")
//            is BottomSheetUiAction.OnDescriptionChange -> descriptionChanged("")
//            is BottomSheetUiAction.OnCompletionChange -> completionChanged(false)
//            is BottomSheetUiAction.BottomSheetClosed -> bottomSheetClosed()
//        }
//    }

    fun copyCellData(bandalartId: Long, cellData: BandalartCellUiModel) {
        viewModelScope.launch {
            val bandalart = bandalartRepository.getBandalart(bandalartId)
            _uiState.update {
                it.copy(
                    bandalartData = bandalart.toUiModel(),
                    cellData = cellData,
                    cellDataForCheck = cellData.copy(),
                    isCellDataCopied = true,
                )
            }
        }
    }

    fun updateBandalartMainCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartMainCellModel: UpdateBandalartMainCellModel,
    ) {
        viewModelScope.launch {
            bandalartRepository.updateBandalartMainCell(bandalartId, cellId, updateBandalartMainCellModel.toEntity())
            bandalartRepository.getBandalart(bandalartId).let { bandalart ->
                _uiState.update {
                    it.copy(
                        bandalartData = it.bandalartData.copy(
                            mainColor = bandalart.mainColor,
                            subColor = bandalart.subColor,
                        ),
                        isCellUpdated = true,
                    )
                }
            }
        }
    }

    fun updateBandalartSubCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartSubCellModel: UpdateBandalartSubCellModel,
    ) {
        viewModelScope.launch {
            bandalartRepository.updateBandalartSubCell(bandalartId, cellId, updateBandalartSubCellModel.toEntity())
            _uiState.update { it.copy(isCellUpdated = true) }
        }
    }

    fun updateBandalartTaskCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartTaskCellModel: UpdateBandalartTaskCellModel,
    ) {
        viewModelScope.launch {
            bandalartRepository.updateBandalartTaskCell(bandalartId, cellId, updateBandalartTaskCellModel.toEntity())
            _uiState.update { it.copy(isCellUpdated = true) }
        }
    }

    fun deleteBandalartCell(cellId: Long) {
        viewModelScope.launch {
            bandalartRepository.deleteBandalartCell(cellId)
        }
    }

    fun toggleDeleteCellDialog(flag: Boolean) {
        _uiState.update { it.copy(isDeleteCellDialogOpened = flag) }
    }

    fun toggleDatePicker(flag: Boolean) {
        _uiState.update { it.copy(isDatePickerOpened = flag) }
    }

    fun toggleEmojiPicker(flag: Boolean) {
        _uiState.update { it.copy(isEmojiPickerOpened = flag) }
    }

    fun emojiSelected(profileEmoji: String?) {
        _uiState.update {
            it.copy(bandalartData = it.bandalartData.copy(profileEmoji = profileEmoji))
        }
    }

    fun titleChanged(title: String) {
        _uiState.update { it.copy(cellData = it.cellData.copy(title = title)) }
    }

    fun colorChanged(mainColor: String, subColor: String) {
        _uiState.update {
            it.copy(
                bandalartData = it.bandalartData.copy(
                    mainColor = mainColor,
                    subColor = subColor,
                ),
            )
        }
    }

    fun dueDateChanged(dueDate: String?) {
        _uiState.update { it.copy(cellData = it.cellData.copy(dueDate = dueDate)) }
    }

    fun descriptionChanged(description: String?) {
        _uiState.update { it.copy(cellData = it.cellData.copy(description = description)) }
    }

    fun completionChanged(flag: Boolean) {
        _uiState.update {
            it.copy(cellData = it.cellData.copy(isCompleted = flag))
        }
    }

    fun bottomSheetClosed() {
        _uiState.update { BottomSheetUiState() }
    }
}
