package com.nexters.bandalart.feature.complete.viewmodel

import androidx.compose.ui.graphics.ImageBitmap

sealed interface CompleteUiAction {
    data object OnShareButtonClick : CompleteUiAction
    data object OnBackButtonClick : CompleteUiAction
    data class ShareBandalart(val bitmap: ImageBitmap): CompleteUiAction
}
