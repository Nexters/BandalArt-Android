package com.nexters.bandalart.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.android.core.common.UiText
import com.nexters.bandalart.android.core.domain.repository.BandalartRepository
import com.nexters.bandalart.android.feature.home.mapper.toEntity
import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartMainCellModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartSubCellModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartTaskCellModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    val cellData: BandalartCellUiModel = BandalartCellUiModel(),
    val cellDataForCheck: BandalartCellUiModel = BandalartCellUiModel(),
    val isCellDataCopied: Boolean = false,
    val isCellUpdated: Boolean = false,
    val isCellDeleted: Boolean = false,
    val isDatePickerOpened: Boolean = false,
    val isEmojiPickerOpened: Boolean = false,
    val isDeleteCellDialogOpened: Boolean = false,
)

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
    private val bandalartRepository: BandalartRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BottomSheetUiState())
    val uiState: StateFlow<BottomSheetUiState> = _uiState.asStateFlow()

    fun copyCellData(cellData: BandalartCellUiModel) {
        _uiState.update {
            it.copy(
                cellData = cellData,
                cellDataForCheck = cellData.copy(),
                isCellDataCopied = true,
            )
        }
    }

    fun updateBandalartMainCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartMainCellModel: UpdateBandalartMainCellModel,
    ) {
        viewModelScope.launch {
            bandalartRepository.updateBandalartMainCell(bandalartId, cellId, updateBandalartMainCellModel.toEntity())
            _uiState.update { it.copy(isCellUpdated = true) }
        }
    }

    fun updateBandalartSubCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartSubCellModel: UpdateBandalartSubCellModel,
    ) {
        viewModelScope.launch {
            bandalartRepository.updateBandalartSubCell(bandalartId, cellId, updateBandalartSubCellModel.toEntity())
            _uiState.update { it.copy(isCellUpdated = true) }
        }
    }

    fun updateBandalartTaskCell(
        bandalartId: Long,
        cellId: Long,
        updateBandalartTaskCellModel: UpdateBandalartTaskCellModel,
    ) {
        viewModelScope.launch {
            bandalartRepository.updateBandalartTaskCell(bandalartId, cellId, updateBandalartTaskCellModel.toEntity())
            _uiState.update { it.copy(isCellUpdated = true) }
        }
    }

    fun deleteBandalartCell(
        bandalartId: Long,
        cellId: Long,
    ) {
        viewModelScope.launch {
            bandalartRepository.deleteBandalartCell(bandalartId, cellId)
        }
    }

    fun openDeleteCellDialog(flag: Boolean) {
        _uiState.update { it.copy(isDeleteCellDialogOpened = flag) }
    }

    fun openDatePicker(flag: Boolean) {
        _uiState.update { it.copy(isDatePickerOpened = flag) }
    }

    fun openEmojiPicker(flag: Boolean) {
        _uiState.update { it.copy(isEmojiPickerOpened = flag) }
    }

    fun emojiSelected(profileEmoji: String?) {
        _uiState.update {
            it.copy(cellData = it.cellData.copy(profileEmoji = profileEmoji))
        }
    }

    fun titleChanged(title: String) {
        _uiState.update { it.copy(cellData = it.cellData.copy(title = title)) }
    }

    fun colorChanged(mainColor: String, subColor: String) {
        _uiState.update {
            it.copy(
                cellData = it.cellData.copy(
                    mainColor = mainColor,
                    subColor = subColor,
                ),
            )
        }
    }

    fun dueDateChanged(dueDate: String?) {
        _uiState.update { it.copy(cellData = it.cellData.copy(dueDate = dueDate)) }
    }

    fun descriptionChanged(description: String?) {
        _uiState.update { it.copy(cellData = it.cellData.copy(description = description)) }
    }

    fun completionChanged(flag: Boolean) {
        _uiState.update {
            it.copy(cellData = it.cellData.copy(isCompleted = flag))
        }
    }

    fun bottomSheetClosed() {
        _uiState.update { BottomSheetUiState() }
    }
}
