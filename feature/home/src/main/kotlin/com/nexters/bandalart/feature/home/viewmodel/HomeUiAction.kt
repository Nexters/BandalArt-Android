package com.nexters.bandalart.feature.home.viewmodel

sealed interface HomeUiAction {
    data class GetHomeList(val bandalartId: Long? = null) : HomeUiAction
    data class GetHomeDetail(val bandalartId: Long, val isBandalartCompleted: Boolean = false) :
        HomeUiAction
    data class CreateHome(val bandalartId: Long? = null) : HomeUiAction
    data class DeleteHome(val bandalartId: Long) : HomeUiAction
    data class UpdateHomeEmoji(
        val bandalartId: Long,
        val cellId: Long,
        val updateBandalartEmojiModel: com.nexters.bandalart.feature.home.model.UpdateBandalartEmojiModel,
    ) : HomeUiAction
    data object OnShareButtonClick : HomeUiAction
    data object OnDropDownMenuClick : HomeUiAction
    data object OpenHomeDeleteAlertDialog : HomeUiAction
    data object OpenEmojiBottomSheet : HomeUiAction
    data object OpenCellBottomSheet : HomeUiAction
    data object BottomSheetDataChanged : HomeUiAction
    data object ShowSkeletonChanged : HomeUiAction
    data object OpenHomeListBottomSheet : HomeUiAction
    data object NavigateToComplete : HomeUiAction
}
