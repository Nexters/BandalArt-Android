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
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
    private val bandalartRepository: BandalartRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BottomSheetUiState())
    val uiState: StateFlow<BottomSheetUiState> = _uiState.asStateFlow()

    fun onAction(action: BottomSheetUiAction) {
        when (action) {
            is BottomSheetUiAction.CopyCellData -> {}
            is BottomSheetUiAction.UpdateBandalartMainCell -> {}
            is BottomSheetUiAction.UpdateBandalartSubCell -> {}
            is BottomSheetUiAction.UpdateBandalartTaskCell -> {}
            is BottomSheetUiAction.OnDeleteDialogConfirmClick -> {
                deleteBandalartCell(action.id)
                toggleDeleteCellDialog(false)
            }

            is BottomSheetUiAction.OpenDeleteCellDialog -> toggleDeleteCellDialog(true)
            is BottomSheetUiAction.OpenDatePicker -> toggleDatePicker(true)
            is BottomSheetUiAction.OpenEmojiPicker -> toggleEmojiPicker(true)
            is BottomSheetUiAction.OnEmojiSelect -> updateEmoji(action.emoji)
            is BottomSheetUiAction.OnModalConfirmClick -> {}
            is BottomSheetUiAction.OnColorSelect -> colorChanged("", "")
            is BottomSheetUiAction.OnDueDateChange -> dueDateChanged("")
            is BottomSheetUiAction.OnTitleUpdate -> updateTitle(action.title, action.currentLocale)
            is BottomSheetUiAction.OnDescriptionUpdate -> updateDescription(action.description)
            is BottomSheetUiAction.OnCompletionChange -> completionChanged(false)
            is BottomSheetUiAction.BottomSheetClosed -> bottomSheetClosed()
        }
    }

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

    private fun deleteBandalartCell(cellId: Long) {
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

    private fun updateEmoji(profileEmoji: String?) {
        _uiState.update { it.copy(bandalartData = it.bandalartData.copy(profileEmoji = profileEmoji)) }
    }

    private fun updateTitle(newTitle: String, currentLocale: Locale) {
        val validatedTitle = validateTitleLength(
            newTitle = newTitle,
            currentTitle = _uiState.value.cellData.title ?: "",
            currentLocale = currentLocale
        )

        _uiState.update {
            it.copy(cellData = it.cellData.copy(title = validatedTitle))
        }
    }

    private fun validateTitleLength(
        newTitle: String,
        currentTitle: String,
        currentLocale: Locale
    ): String {
        val maxLength = when (currentLocale.language) {
            Locale.KOREAN.language -> 15
            else -> 24
        }

        return if (newTitle.length > maxLength) currentTitle else newTitle
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

    private fun updateDescription(newDescription: String?) {
        val validatedDescription = validateDescriptionLength(
            newDescription = newDescription,
            currentDescription = _uiState.value.cellData.description
        )

        _uiState.update {
            it.copy(cellData = it.cellData.copy(description = validatedDescription))
        }
    }

    private fun validateDescriptionLength(
        newDescription: String?,
        currentDescription: String?
    ): String? {
        return if ((newDescription?.length ?: 0) > 1000) currentDescription else newDescription
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
