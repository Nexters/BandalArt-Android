package com.nexters.bandalart.feature.home.viewmodel

import java.util.Locale

sealed interface BottomSheetUiAction {
    data object CopyCellData : BottomSheetUiAction
    data object UpdateBandalartMainCell : BottomSheetUiAction
    data object UpdateBandalartSubCell : BottomSheetUiAction
    data object UpdateBandalartTaskCell : BottomSheetUiAction
    data class OnDeleteDialogConfirmClick(val id: Long) : BottomSheetUiAction
    data object OpenDeleteCellDialog : BottomSheetUiAction
    data object OpenDatePicker : BottomSheetUiAction
    data object OpenEmojiPicker : BottomSheetUiAction
    data class OnEmojiSelect(val emoji: String) : BottomSheetUiAction
    data object OnModalConfirmClick : BottomSheetUiAction
    data object OnColorSelect : BottomSheetUiAction
    data object OnDueDateChange : BottomSheetUiAction
    data class OnTitleUpdate(val title: String, val currentLocale: Locale): BottomSheetUiAction
    data class OnDescriptionUpdate(val description: String) : BottomSheetUiAction
    data object OnCompletionChange : BottomSheetUiAction
    data object BottomSheetClosed : BottomSheetUiAction
}
