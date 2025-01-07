package com.nexters.bandalart.feature.home.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import com.nexters.bandalart.core.common.utils.UiText
import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.feature.home.model.CellType
import java.util.Locale

sealed interface HomeUiEvent {
    // HomeScreen UiEvent
    data class NavigateToComplete(
        val id: Long,
        val title: String,
        val profileEmoji: String,
        val bandalartChartImageUri: String,
    ) : HomeUiEvent

    data class ShowSnackbar(val message: UiText) : HomeUiEvent
    data class ShowToast(val message: UiText) : HomeUiEvent
    data class SaveBandalart(val bitmap: ImageBitmap) : HomeUiEvent
    data class ShareBandalart(val bitmap: ImageBitmap) : HomeUiEvent
    data class CaptureBandalart(val bitmap: ImageBitmap) : HomeUiEvent
    data object ShowAppVersion : HomeUiEvent

    // HomeScreen UiAction
    data object OnListClick : HomeUiEvent
    data object OnSaveClick : HomeUiEvent
    data object OnDeleteClick : HomeUiEvent
    data class OnEmojiSelected(
        val bandalartId: Long,
        val cellId: Long,
        val updateBandalartEmojiModel: UpdateBandalartEmojiEntity,
    ) : HomeUiEvent

    data object OnShareButtonClick : HomeUiEvent
    data object OnAddClick : HomeUiEvent
    data object OnMenuClick : HomeUiEvent
    data object OnDropDownMenuDismiss : HomeUiEvent
    data object OnEmojiClick : HomeUiEvent
    data class OnBandalartListItemClick(val key: Long) : HomeUiEvent
    data class OnBandalartCellClick(
        val cellType: CellType,
        val isMainCellTitleEmpty: Boolean,
        val cellData: BandalartCellEntity,
    ) : HomeUiEvent

    data object OnCloseButtonClick : HomeUiEvent
    data object OnAppTitleClick : HomeUiEvent

    // BottomSheet UiAction
    data object OnDismiss : HomeUiEvent
    data class OnCellTitleUpdate(val title: String, val locale: Locale) : HomeUiEvent
    data class OnEmojiSelect(val emoji: String) : HomeUiEvent
    data class OnColorSelect(val mainColor: String, val subColor: String) : HomeUiEvent
    data object OnDatePickerClick : HomeUiEvent
    data class OnDueDateSelect(val date: String) : HomeUiEvent
    data class OnDescriptionUpdate(val description: String) : HomeUiEvent
    data class OnCompletionUpdate(val isCompleted: Boolean) : HomeUiEvent
    data class OnDeleteBandalart(val bandalartId: Long) : HomeUiEvent
    data class OnDeleteCell(val cellId: Long) : HomeUiEvent
    data object OnCancelClick : HomeUiEvent
    data object OnEmojiPickerClick : HomeUiEvent
    data object OnCloseBottomSheet : HomeUiEvent
    data object OnDeleteButtonClick : HomeUiEvent
    data class OnCompleteButtonClick(
        val bandalartId: Long,
        val cellId: Long,
        val cellType: CellType,
    ) : HomeUiEvent
}
