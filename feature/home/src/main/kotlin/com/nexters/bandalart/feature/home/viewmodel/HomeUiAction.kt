package com.nexters.bandalart.feature.home.viewmodel

import com.nexters.bandalart.core.domain.entity.UpdateBandalartEmojiEntity

sealed interface HomeUiAction {
    data class OnCreateClick(val bandalartId: Long? = null) : HomeUiAction
    data object OnListClick : HomeUiAction
    data object OnDeleteClick : HomeUiAction
    data class OnEmojiSelected(
        val bandalartId: Long,
        val cellId: Long,
        val updateBandalartEmojiModel: UpdateBandalartEmojiEntity,
    ) : HomeUiAction

    data class OnConfirmClick(val modalType: ModalType) : HomeUiAction
    data class OnCancelClick(val modalType: ModalType) : HomeUiAction
    data object OnShareButtonClick : HomeUiAction
    data object OnAddClick : HomeUiAction
    data class ToggleDropDownMenu(val flag: Boolean) : HomeUiAction
    data class ToggleDeleteAlertDialog(val flag: Boolean) : HomeUiAction
    data class ToggleEmojiBottomSheet(val flag: Boolean) : HomeUiAction
    data class ToggleCellBottomSheet(val flag: Boolean) : HomeUiAction
    data class ToggleBandalartListBottomSheet(val flag: Boolean) : HomeUiAction
    data object BottomSheetDataChanged : HomeUiAction
    data class OnBandalartListItemClick(val key: Long) : HomeUiAction
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
