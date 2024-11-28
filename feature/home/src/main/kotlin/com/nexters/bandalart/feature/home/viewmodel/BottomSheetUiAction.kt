package com.nexters.bandalart.feature.home.viewmodel

import com.nexters.bandalart.feature.home.model.CellType
import java.util.Locale

sealed interface BottomSheetUiAction {
    data object OnClearButtonClick : BottomSheetUiAction
    data class OnCompleteButtonClick(
        val bandalartId: Long,
        val cellId: Long,
        val cellType: CellType,
    ) : BottomSheetUiAction

    data object OnDeleteButtonClick : BottomSheetUiAction
    data class OnDeleteDialogConfirmClick(val id: Long) : BottomSheetUiAction
    data object OnDeleteDialogCancelClick : BottomSheetUiAction
    data object OnDatePickerClick : BottomSheetUiAction
    data object OnEmojiPickerClick : BottomSheetUiAction
    data class OnEmojiSelect(val emoji: String) : BottomSheetUiAction
    data class OnColorSelect(val mainColor: String, val subColor: String) : BottomSheetUiAction
    data class OnDueDateSelect(val dueDate: String) : BottomSheetUiAction
    data class OnTitleUpdate(val title: String, val currentLocale: Locale) : BottomSheetUiAction
    data class OnDescriptionUpdate(val description: String) : BottomSheetUiAction
    data class OnCompletionUpdate(val isCompleted: Boolean) : BottomSheetUiAction
}
