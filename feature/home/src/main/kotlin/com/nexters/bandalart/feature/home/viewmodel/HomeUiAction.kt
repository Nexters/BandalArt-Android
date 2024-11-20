package com.nexters.bandalart.feature.home.viewmodel

sealed interface HomeUiAction {
    data class OnCreateClick(val bandalartId: Long? = null) : HomeUiAction
    data object OnListClick: HomeUiAction
    data class OnDeleteClick(val bandalartId: Long) : HomeUiAction
    data class UpdateEmojiClick(
        val bandalartId: Long,
        val cellId: Long,
        val updateBandalartEmojiModel: com.nexters.bandalart.feature.home.model.UpdateBandalartEmojiModel,
    ) : HomeUiAction
    data class OnCancelClick(val modalType: ModalType) : HomeUiAction
    data object OnShareButtonClick : HomeUiAction
    data object OnDropDownMenuClick : HomeUiAction
    data object OpenDeleteAlertDialog : HomeUiAction
    data object OnEmojiBoxClick : HomeUiAction
    data object OpenCellBottomSheet : HomeUiAction
    data object BottomSheetDataChanged : HomeUiAction
    data object ShowSkeletonChanged : HomeUiAction
    data object OpenHomeListBottomSheet : HomeUiAction
    data object NavigateToComplete : HomeUiAction
}

enum class ModalType {
    // 셀 정보 수정 바텀시트
    CELL,
    // 이모지 선택 바텀시트
    EMOJI,
    // 반다라트 목록 바텀시트
    BANDALART_LIST,
    // 삭제 다이얼로그
    DELETE_DIALOG
}
