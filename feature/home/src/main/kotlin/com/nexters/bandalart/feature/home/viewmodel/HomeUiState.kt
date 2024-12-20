package com.nexters.bandalart.feature.home.viewmodel

import com.nexters.bandalart.core.domain.entity.BandalartCellEntity
import com.nexters.bandalart.core.ui.UiState
import com.nexters.bandalart.feature.home.model.BandalartUiModel
import com.nexters.bandalart.feature.home.model.CellType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * HomeUiState
 *
 * @param bandalartList 반다라트 목록
 * @param bandalartData 반다라트 데이터,
 * @param bandalartCellData 반다라트 각각의 셀의 데이터,
 * @param isDropDownMenuOpened 드롭 다운 메뉴가 열림
 * @param isBandalartDeleteAlertDialogOpened 반다라트 표 삭제 다이얼로그가 열림
 * @param isBandalartListBottomSheetOpened 반다라트 목록 바텀시트가 열림
 * @param isCellBottomSheetOpened 반다라트 셀 바텀시트가 열림
 * @param isEmojiBottomSheetOpened 반다라트 이모지 바텀시트가 열림
 * @param isBandalartCompleted 반다라트 목표를 달성함
 * @param isShowSkeleton 표의 첫 로딩을 보여주는 스켈레톤 이미지
 * @param isShared 반다라트가 공유되었는지 유무
 * @param isCaptured 반다라트가 완료되어서 캡쳐되었는지 유무
 * @param clickedCellType 클릭한 반다라트 셀의 타입(메인, 서브, 태스크)
 */

sealed interface HomeUiState : UiState {
    data class Content(
        // 기본 홈 화면 상태
        val bandalartList: ImmutableList<BandalartUiModel> = persistentListOf(),
        val bandalartData: BandalartUiModel? = null,
        val bandalartCellData: BandalartCellEntity? = null,
        val isShowSkeleton: Boolean = false,
        val bandalartChartUrl: String? = null,
        val isBandalartCompleted: Boolean = false,

        // UI 상태 관리
        val modal: ModalState = ModalState.Hidden,
        val shareState: ShareState = ShareState.None,

        // 선택된 셀 정보
        val clickedCellType: CellType = CellType.MAIN,
        val clickedCellData: BandalartCellEntity? = null,
    ) : HomeUiState
}

sealed interface ModalState : UiState {
    data object Hidden : ModalState
    data object DropDownMenu : ModalState
    data class Modals(
        val bottomSheet: BottomSheetModal? = null,
        val dialog: DialogModal? = null,
    ) : ModalState
}

sealed interface BottomSheetModal {
    data class Cell(val data: BottomSheetData) : BottomSheetModal
    data class BandalartList(val data: BottomSheetData) : BottomSheetModal
    data class Emoji(val data: BottomSheetData) : BottomSheetModal
}

sealed interface DialogModal {
    data object Delete : DialogModal
}

data class BottomSheetData(
    // BottomSheet 공통 데이터
    val initialCellData: BandalartCellEntity = BandalartCellEntity(),
    val cellData: BandalartCellEntity = BandalartCellEntity(),
    val initialBandalartData: BandalartUiModel = BandalartUiModel(),
    val bandalartData: BandalartUiModel = BandalartUiModel(),

    // UI 상태
    val isDatePickerOpened: Boolean = false,
    val isEmojiPickerOpened: Boolean = false,
    val isDeleteCellDialogOpened: Boolean = false,
)

sealed interface ShareState : UiState {
    data object None : ShareState
    data object Share : ShareState
    data object Capture : ShareState
}
