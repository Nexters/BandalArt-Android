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
    data object EmojiSelected : BottomSheetUiAction
    data object TitleChanged : BottomSheetUiAction
    data object ColorChanged : BottomSheetUiAction
    data object DueDateChanged : BottomSheetUiAction
    data object DescriptionChanged : BottomSheetUiAction
    data object CompletionChanged : BottomSheetUiAction
    data object BottomSheetClosed : BottomSheetUiAction
}
