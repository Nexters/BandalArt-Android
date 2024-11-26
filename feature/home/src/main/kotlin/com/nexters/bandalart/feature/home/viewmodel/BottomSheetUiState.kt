package com.nexters.bandalart.feature.home.viewmodel

import com.nexters.bandalart.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.feature.home.model.BandalartUiModel

/**
 * BottomSheetUiState
 *
 * @param cellData 반다라트 표의 현재 선택된 데이터
 * @param initialCellData 반다라트 표의 데이터 완료 버튼 활성화를 위한 Cell 의 초기 데이터
 * @param isCellDataCopied 데이터 copy 가 됐는지 판단함
 * @param isCellUpdated 반다라트 표의 특정 셀이 수정됨
 * @param isCellDeleted 반다라트 표의 특정 셀의 삭제됨(비어있는 셀로 전환)
 * @param isDatePickerOpened 데이트 피커가 열림
 * @param isEmojiPickerOpened 이모지 피커가 열림
 * @param isDeleteCellDialogOpened 셀 삭제 시, 경고 창이 열림
 */

data class BottomSheetUiState(
    val bandalartData: BandalartUiModel = BandalartUiModel(),
    val cellData: BandalartCellUiModel = BandalartCellUiModel(),
    val initialCellData: BandalartCellUiModel = BandalartCellUiModel(),
    val isCellDataCopied: Boolean = false,
    val isCellUpdated: Boolean = false,
    val isCellDeleted: Boolean = false,
    val isDatePickerOpened: Boolean = false,
    val isEmojiPickerOpened: Boolean = false,
    val isDeleteCellDialogOpened: Boolean = false,
)
