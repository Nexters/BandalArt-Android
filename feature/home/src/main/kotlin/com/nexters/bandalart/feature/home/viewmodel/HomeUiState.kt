package com.nexters.bandalart.feature.home.viewmodel

import com.nexters.bandalart.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.feature.home.model.BandalartUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * HomeUiState
 *
 * @param bandalartList 반다라트 목록
 * @param bandalartData 반다라트 데이터,
 * @param bandalartCellData 반다라트 각각의 셀의 데이터,
 * @param isBandalartDeleted 표가 삭제됨
 * @param isDropDownMenuOpened 드롭 다운 메뉴가 열림
 * @param isBandalartDeleteAlertDialogOpened 반다라트 표 삭제 다이얼로그가 열림
 * @param isBandalartListBottomSheetOpened 반다라트 목록 바텀시트가 열림
 * @param isCellBottomSheetOpened 반다라트 셀 바텀시트가 열림
 * @param isEmojiBottomSheetOpened 반다라트 이모지 바텀시트가 열림
 * @param isBottomSheetDataChanged 바텀시트의 데이터가 변경됨
 * @param isBottomSheetMainCellChanged 바텀시트의 변경된 데이터가 메인 셀임
 * @param isBandalartCompleted 반다라트 목표를 달성함
 * @param isShowSkeleton 표의 첫 로딩을 보여주는 스켈레톤 이미지
 */

data class HomeUiState(
    val bandalartList: ImmutableList<BandalartUiModel> = persistentListOf(),
    val bandalartData: BandalartUiModel? = null,
    val bandalartCellData: BandalartCellUiModel? = null,
    val isBandalartDeleted: Boolean = false,
    val isDropDownMenuOpened: Boolean = false,
    val isBandalartDeleteAlertDialogOpened: Boolean = false,
    val isBandalartListBottomSheetOpened: Boolean = false,
    val isCellBottomSheetOpened: Boolean = false,
    val isEmojiBottomSheetOpened: Boolean = false,
    val isBottomSheetDataChanged: Boolean = false,
    val isBottomSheetMainCellChanged: Boolean = false,
    val isBandalartCompleted: Boolean = false,
    val isShowSkeleton: Boolean = false,
    val isShared: Boolean = false,
    val isCaptured: Boolean = false,
    val bandalartChartUrl: String? = null,
)
