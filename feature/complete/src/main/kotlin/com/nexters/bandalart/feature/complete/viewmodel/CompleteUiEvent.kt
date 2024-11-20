package com.nexters.bandalart.feature.complete.viewmodel

import androidx.compose.ui.graphics.ImageBitmap

sealed interface CompleteUiEvent {
    data object NavigateBack : CompleteUiEvent
    data class ShareBandalart(val bitmap: ImageBitmap) : CompleteUiEvent
}
