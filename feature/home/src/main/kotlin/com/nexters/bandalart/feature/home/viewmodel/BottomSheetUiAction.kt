package com.nexters.bandalart.feature.home.viewmodel

sealed interface BottomSheetUiAction {
    data object CopyCellData : BottomSheetUiAction
    data object UpdateBandalartMainCell : BottomSheetUiAction
    data object UpdateBandalartSubCell : BottomSheetUiAction
    data object UpdateBandalartTaskCell : BottomSheetUiAction
    data object DeleteBandalartCell : BottomSheetUiAction
    data object OpenDeleteCellDialog : BottomSheetUiAction
    data object OpenDatePicker : BottomSheetUiAction
    data object OpenEmojiPicker : BottomSheetUiAction
    data object OnModalConfirmClick : BottomSheetUiAction
    data object TitleChanged : BottomSheetUiAction
    data object OnColorSelect : BottomSheetUiAction
    data object OnDueDateChange : BottomSheetUiAction
    data object OnDescriptionChange : BottomSheetUiAction
    data object OnCompletionChange : BottomSheetUiAction
    data object BottomSheetClosed : BottomSheetUiAction
}
