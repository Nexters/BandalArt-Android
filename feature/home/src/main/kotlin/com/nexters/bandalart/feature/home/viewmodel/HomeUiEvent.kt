package com.nexters.bandalart.feature.home.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import com.nexters.bandalart.core.common.utils.UiText
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.feature.home.model.CellType

sealed interface HomeUiEvent {
    data class NavigateToComplete(
        val id: Long,
        val title: String,
        val profileEmoji: String,
        val bandalartChart: String,
    ) : HomeUiEvent

    data class ShowSnackbar(val message: UiText) : HomeUiEvent
    data class ShowToast(val message: UiText) : HomeUiEvent
    data class SaveBandalart(val bitmap: ImageBitmap) : HomeUiEvent
    data class ShareBandalart(val bitmap: ImageBitmap) : HomeUiEvent
    data class CaptureBandalart(val bitmap: ImageBitmap) : HomeUiEvent
    data object ShowAppVersion : HomeUiEvent

    // Action 관련 이벤트들
    data object OnListClick : HomeUiEvent
    data object OnSaveClick : HomeUiEvent
    data object OnDeleteClick : HomeUiEvent
    data class OnEmojiSelected(
        val bandalartId: Long,
        val cellId: Long,
        val updateBandalartEmojiModel: UpdateBandalartEmojiEntity
    ) : HomeUiEvent
    data class OnConfirmClick(val modalType: ModalType) : HomeUiEvent
    data class OnCancelClick(val modalType: ModalType) : HomeUiEvent
    data object OnShareButtonClick : HomeUiEvent
    data object OnAddClick : HomeUiEvent
    data class ToggleDropDownMenu(val flag: Boolean) : HomeUiEvent
    data class ToggleDeleteAlertDialog(val flag: Boolean) : HomeUiEvent
    data class ToggleEmojiBottomSheet(val flag: Boolean) : HomeUiEvent
    data class ToggleCellBottomSheet(val flag: Boolean) : HomeUiEvent
    data class ToggleBandalartListBottomSheet(val flag: Boolean) : HomeUiEvent
    data class OnBandalartListItemClick(val key: Long) : HomeUiEvent
    data class OnBandalartCellClick(
        val cellType: CellType,
        val isMainCellTitleEmpty: Boolean,
        val cellData: BandalartCellEntity
    ) : HomeUiEvent
    data object OnCloseButtonClick : HomeUiEvent
    data object OnAppTitleClick : HomeUiEvent
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
