package com.nexters.bandalart.feature.complete.viewmodel

import android.net.Uri

sealed interface CompleteUiEvent {
    data object NavigateBack : CompleteUiEvent
    data class ShareBandalart(val imageUri: Uri) : CompleteUiEvent
}
