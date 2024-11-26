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
            is BottomSheetUiAction.OnCompleteButtonClick -> updateCell(action.bandalartId, action.cellId, action.isMainCell, action.isSubCell)
            is BottomSheetUiAction.OnDeleteButtonClick -> toggleDeleteCellDialog(!_uiState.value.isDeleteCellDialogOpened)
            is BottomSheetUiAction.OnDeleteDialogConfirmClick -> {
                deleteBandalartCell(action.id)
                toggleDeleteCellDialog(false)
            }

            is BottomSheetUiAction.OnDeleteDialogCancelClick -> toggleDeleteCellDialog(false)
            is BottomSheetUiAction.OnDatePickerClick -> toggleDatePicker(!_uiState.value.isDatePickerOpened)
            is BottomSheetUiAction.OnEmojiPickerClick -> toggleEmojiPicker(!_uiState.value.isEmojiPickerOpened)
            is BottomSheetUiAction.OnEmojiSelect -> updateEmoji(action.emoji)
            is BottomSheetUiAction.OnColorSelect -> updateThemeColor(action.mainColor, action.subColor)
            is BottomSheetUiAction.OnDueDateSelect -> updateDueDate(action.dueDate)
            is BottomSheetUiAction.OnTitleUpdate -> updateTitle(action.title, action.currentLocale)
            is BottomSheetUiAction.OnDescriptionUpdate -> updateDescription(action.description)
            is BottomSheetUiAction.OnCompletionUpdate -> updateCompletion(action.isCompleted)
        }
    }

    fun copyCellData(bandalartId: Long, cellData: BandalartCellUiModel) {
        viewModelScope.launch {
            val bandalart = bandalartRepository.getBandalart(bandalartId)
            _uiState.update {
                it.copy(
                    bandalartData = bandalart.toUiModel(),
                    initialBandalartData = bandalart.toUiModel(),
                    cellData = cellData,
                    initialCellData = cellData,
                    isCellDataCopied = true,
                )
            }
        }
    }

    private fun updateCell(
        bandalartId: Long,
        cellId: Long,
        isMainCell: Boolean,
        isSubCell: Boolean,
    ) {
        val trimmedTitle = _uiState.value.cellData.title?.trim()
        val description = _uiState.value.cellData.description
        val dueDate = _uiState.value.cellData.dueDate?.ifEmpty { null }

        when {
            isMainCell -> {
                updateMainCell(
                    bandalartId = bandalartId,
                    cellId = cellId,
                    updateBandalartMainCellModel = UpdateBandalartMainCellModel(
                        title = trimmedTitle,
                        description = description,
                        dueDate = dueDate,
                        profileEmoji = _uiState.value.bandalartData.profileEmoji,
                        mainColor = _uiState.value.bandalartData.mainColor,
                        subColor = _uiState.value.bandalartData.subColor,
                    ),
                )
            }

            isSubCell -> {
                updateSubCell(
                    bandalartId = bandalartId,
                    cellId = cellId,
                    updateBandalartSubCellModel = UpdateBandalartSubCellModel(
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
                    updateBandalartTaskCellModel = UpdateBandalartTaskCellModel(
                        title = trimmedTitle,
                        description = description,
                        dueDate = dueDate,
                        isCompleted = _uiState.value.cellData.isCompleted,
                    ),
                )
            }
        }
        _uiState.update { it.copy(isCellUpdated = true) }
    }

    private fun updateMainCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartMainCellModel: UpdateBandalartMainCellModel,
    ) {
        viewModelScope.launch {
            bandalartRepository.updateBandalartMainCell(bandalartId, cellId, updateBandalartMainCellModel.toEntity())
            bandalartRepository.getBandalart(bandalartId)
        }
    }

    private fun updateSubCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartSubCellModel: UpdateBandalartSubCellModel,
    ) {
        viewModelScope.launch {
            bandalartRepository.updateBandalartSubCell(bandalartId, cellId, updateBandalartSubCellModel.toEntity())
        }
    }

    private fun updateTaskCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartTaskCellModel: UpdateBandalartTaskCellModel,
    ) {
        viewModelScope.launch {
            bandalartRepository.updateBandalartTaskCell(bandalartId, cellId, updateBandalartTaskCellModel.toEntity())
        }
    }

    private fun deleteBandalartCell(cellId: Long) {
        viewModelScope.launch {
            bandalartRepository.deleteBandalartCell(cellId)
        }
    }

    private fun toggleDeleteCellDialog(flag: Boolean) {
        _uiState.update { it.copy(isDeleteCellDialogOpened = flag) }
    }

    private fun toggleDatePicker(flag: Boolean) {
        _uiState.update { it.copy(isDatePickerOpened = flag) }
    }

    private fun toggleEmojiPicker(flag: Boolean) {
        _uiState.update { it.copy(isEmojiPickerOpened = flag) }
        if (_uiState.value.isDatePickerOpened) toggleDatePicker(false)
    }

    private fun updateEmoji(profileEmoji: String?) {
        _uiState.update { it.copy(bandalartData = it.bandalartData.copy(profileEmoji = profileEmoji)) }
        toggleEmojiPicker(false)
    }

    private fun updateTitle(newTitle: String, currentLocale: Locale) {
        val validatedTitle = validateTitleLength(
            newTitle = newTitle,
            currentTitle = _uiState.value.cellData.title ?: "",
            currentLocale = currentLocale,
        )

        _uiState.update {
            it.copy(cellData = it.cellData.copy(title = validatedTitle))
        }
    }

    private fun validateTitleLength(
        newTitle: String,
        currentTitle: String,
        currentLocale: Locale,
    ): String {
        val maxLength = when (currentLocale.language) {
            Locale.KOREAN.language -> 15
            else -> 24
        }

        return if (newTitle.length > maxLength) currentTitle else newTitle
    }

    private fun updateThemeColor(mainColor: String, subColor: String) {
        _uiState.update {
            it.copy(
                bandalartData = it.bandalartData.copy(
                    mainColor = mainColor,
                    subColor = subColor,
                ),
            )
        }
    }

    private fun updateDueDate(dueDate: String?) {
        _uiState.update { it.copy(cellData = it.cellData.copy(dueDate = dueDate)) }
        toggleDatePicker(false)
    }

    private fun updateDescription(newDescription: String?) {
        val validatedDescription = validateDescriptionLength(
            newDescription = newDescription,
            currentDescription = _uiState.value.cellData.description,
        )

        _uiState.update {
            it.copy(cellData = it.cellData.copy(description = validatedDescription))
        }
    }

    private fun validateDescriptionLength(
        newDescription: String?,
        currentDescription: String?,
    ): String? {
        return if ((newDescription?.length ?: 0) > 1000) currentDescription else newDescription
    }

    private fun updateCompletion(flag: Boolean) {
        _uiState.update {
            it.copy(cellData = it.cellData.copy(isCompleted = flag))
        }
    }
}
