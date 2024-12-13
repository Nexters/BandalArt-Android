package com.nexters.bandalart.feature.home.viewmodel

import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.domain.entity.UpdateBandalartEmojiEntity
import com.nexters.bandalart.feature.home.model.CellType

//sealed interface HomeUiAction {
//    data object OnListClick : HomeUiAction
//    data object OnSaveClick : HomeUiAction
//    data object OnDeleteClick : HomeUiAction
//    data class OnEmojiSelected(
//        val bandalartId: Long,
//        val cellId: Long,
//        val updateBandalartEmojiModel: UpdateBandalartEmojiEntity,
//    ) : HomeUiAction
//
//    data class OnConfirmClick(val modalType: ModalType) : HomeUiAction
//    data class OnCancelClick(val modalType: ModalType) : HomeUiAction
//    data object OnShareButtonClick : HomeUiAction
//    data object OnAddClick : HomeUiAction
//    data class ToggleDropDownMenu(val flag: Boolean) : HomeUiAction
//    data class ToggleDeleteAlertDialog(val flag: Boolean) : HomeUiAction
//    data class ToggleEmojiBottomSheet(val flag: Boolean) : HomeUiAction
//    data class ToggleCellBottomSheet(val flag: Boolean) : HomeUiAction
//    data class ToggleBandalartListBottomSheet(val flag: Boolean) : HomeUiAction
//    data class OnBandalartListItemClick(val key: Long) : HomeUiAction
//    data class OnBandalartCellClick(
//        val cellType: CellType,
//        val isMainCellTitleEmpty: Boolean,
//        val cellData: BandalartCellEntity,
//    ) : HomeUiAction
//
//    data object OnCloseButtonClick : HomeUiAction
//    data object OnAppTitleClick : HomeUiAction
//}
