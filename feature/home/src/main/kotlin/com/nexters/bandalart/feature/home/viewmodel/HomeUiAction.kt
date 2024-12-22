package com.nexters.bandalart.feature.home.viewmodel

import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.feature.home.model.CellType
import java.util.Locale

sealed interface HomeUiAction {
    // HomeScreen UiAction
    data object OnListClick : HomeUiAction
    data object OnSaveClick : HomeUiAction
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
    data class OnBandalartListItemClick(val key: Long) : HomeUiAction
    data class OnBandalartCellClick(
        val cellType: CellType,
        val isMainCellTitleEmpty: Boolean,
        val cellData: BandalartCellEntity,
    ) : HomeUiAction

    data object OnCloseButtonClick : HomeUiAction
    data object OnAppTitleClick : HomeUiAction

    // BottomSheet UiAction
    data class OnCellTitleUpdate(val title: String, val locale: Locale) : HomeUiAction
    data class OnEmojiSelect(val emoji: String) : HomeUiAction
    data class OnColorSelect(val mainColor: String, val subColor: String) : HomeUiAction
    data object OnDatePickerClick : HomeUiAction
    data class OnDueDateSelect(val date: String) : HomeUiAction
    data class OnDescriptionUpdate(val description: String) : HomeUiAction
    data class OnCompletionUpdate(val isCompleted: Boolean) : HomeUiAction
    data class OnDeleteCell(val cellId: Long) : HomeUiAction
    data object OnCancelDeleteCell : HomeUiAction
    data object OnEmojiPickerClick : HomeUiAction
    data object OnCloseBottomSheet : HomeUiAction
    data object OnDeleteButtonClick : HomeUiAction
    data class OnCompleteButtonClick(
        val bandalartId: Long,
        val cellId: Long,
        val cellType: CellType,
    ) : HomeUiAction
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
