package com.nexters.bandalart.feature.home.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import com.nexters.bandalart.core.common.utils.UiText

sealed interface HomeEvent {
    data class NavigateToComplete(
        val id: Long,
        val title: String,
        val profileEmoji: String,
        val bandalartChart: String,
    ) : HomeEvent

    data class ShowSnackbar(val message: UiText) : HomeEvent
    data class ShowToast(val message: UiText) : HomeEvent
    data class SaveBandalart(val bitmap: ImageBitmap) : HomeEvent
    data class ShareBandalart(val bitmap: ImageBitmap) : HomeEvent
    data class CaptureBandalart(val bitmap: ImageBitmap) : HomeEvent
    data object ShowAppVersion : HomeEvent
}

enum class ModalType {
    // 셀 정보 수정 바텀시트
    CELL,

    // 이모지 선택 바텀시트
    EMOJI,

    // 반다라트 목록 바텀시트
    BANDALART_LIST,

    // 삭제 다이얼로그
    DELETE_DIALOG,
}
