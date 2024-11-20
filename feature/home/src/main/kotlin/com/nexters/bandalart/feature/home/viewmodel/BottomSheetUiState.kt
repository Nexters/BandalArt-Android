package com.nexters.bandalart.feature.home.viewmodel

import com.nexters.bandalart.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.feature.home.model.BandalartUiModel

/**
 * BottomSheetUiState
 *
 * @param cellData 반다라트 표의 데이터, 서버와의 통신을 성공하면 not null
 * @param cellDataForCheck 반다라트 표의 데이터 롼료 버튼 활성화를 위한 copy 데이터
 * @param isCellDataCopied 데이터 copy가 됐는지 판단함
 * @param isCellUpdated 반다라트 표의 특정 셀이 수정됨
 * @param isCellDeleted 반다라트의 표의 특정 셀의 삭제됨(비어있는 셀로 전환)
 * @param isDatePickerOpened 데이트 피커가 열림
 * @param isEmojiPickerOpened 이모지 피커가 열림
 * @param isDeleteCellDialogOpened 셀 삭제 시, 경고 창이 열림
 */

data class BottomSheetUiState(
    val bandalartData: BandalartUiModel = BandalartUiModel(),
    val cellData: BandalartCellUiModel = BandalartCellUiModel(),
    val cellDataForCheck: BandalartCellUiModel = BandalartCellUiModel(),
    val isCellDataCopied: Boolean = false,
    val isCellUpdated: Boolean = false,
    val isCellDeleted: Boolean = false,
    val isDatePickerOpened: Boolean = false,
    val isEmojiPickerOpened: Boolean = false,
    val isDeleteCellDialogOpened: Boolean = false,
)

