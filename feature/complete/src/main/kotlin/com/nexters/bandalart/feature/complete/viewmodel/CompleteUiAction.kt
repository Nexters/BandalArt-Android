package com.nexters.bandalart.feature.complete.viewmodel

sealed interface CompleteUiAction {
    data object OnShareButtonClick : CompleteUiAction
    data object OnBackButtonClick : CompleteUiAction
}
