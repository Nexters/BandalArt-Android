package com.nexters.bandalart.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.android.core.domain.usecase.bandalart.DeleteBandalartCellUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.UpdateBandalartMainCellUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.UpdateBandalartSubCellUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.UpdateBandalartTaskCellUseCase
import com.nexters.bandalart.android.feature.home.mapper.toEntity
import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartTaskCellModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartMainCellModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartSubCellModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * BottomSheetUiState
 *
 * @param cellData 반다라트 표의 데이터, 서버와의 통신을 성공하면 not null
 * @param isCellUpdated 반다라트 표의 특정 셀이 수정됨
 * @param isCellDeleted 반다라트의 표의 특정 셀의 삭제됨(비어있는 셀로 전환)
 * @param isDatePickerOpened 데이트 피커가 열림
 * @param isEmojiPickerOpened 이모지 피커가 열림
 * @param isDeleteCellDialogOpened 셀 삭제 시, 경고 창이 열림
 * @param error 서버와의 통신을 실패
 */

// TODO Token 확인 로직의 위치 결정
data class BottomSheetUiState(
  val cellData: BandalartCellUiModel = BandalartCellUiModel(),
  val isCellDataCopied: Boolean = false,
  val isCellUpdated: Boolean = false,
  val isCellDeleted: Boolean = false,
  val isDatePickerOpened: Boolean = false,
  val isEmojiPickerOpened: Boolean = false,
  val isDeleteCellDialogOpened: Boolean = false,
  val error: Throwable? = null,
)

sealed class BottomSheetUiEvent {
  data class ShowSnackbar(val message: String) : BottomSheetUiEvent()
}

@Suppress("unused")
@HiltViewModel
class BottomSheetViewModel @Inject constructor(
  private val updateBandalartMainCellUseCase: UpdateBandalartMainCellUseCase,
  private val updateBandalartSubCellUseCase: UpdateBandalartSubCellUseCase,
  private val updateBandalartTaskCellUseCase: UpdateBandalartTaskCellUseCase,
  private val deleteBandalartCellUseCase: DeleteBandalartCellUseCase,
) : ViewModel() {

  private val _bottomSheetState = MutableStateFlow(BottomSheetUiState())
  val bottomSheetState: StateFlow<BottomSheetUiState> = this._bottomSheetState.asStateFlow()

  private val _eventFlow = MutableSharedFlow<BottomSheetUiEvent>()
  val eventFlow: SharedFlow<BottomSheetUiEvent> = _eventFlow.asSharedFlow()

  fun copyCellData(cellData: BandalartCellUiModel) {
    _bottomSheetState.update {
      it.copy(
        cellData = cellData,
        isCellDataCopied = true,
      )
    }
  }

  fun updateBandalartMainCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartMainCellModel: UpdateBandalartMainCellModel,
  ) {
    viewModelScope.launch {
      val result = updateBandalartMainCellUseCase(bandalartKey, cellKey, updateBandalartMainCellModel.toEntity())
      when {
        result.isSuccess && result.getOrNull() != null -> { }
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _bottomSheetState.value = _bottomSheetState.value.copy(error = exception)
          _eventFlow.emit(BottomSheetUiEvent.ShowSnackbar("${exception.message}"))
          Timber.e(exception)
        }
      }
    }
  }

  fun updateBandalartSubCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartSubCellModel: UpdateBandalartSubCellModel,
  ) {
    viewModelScope.launch {
      val result = updateBandalartSubCellUseCase(bandalartKey, cellKey, updateBandalartSubCellModel.toEntity())
      when {
        result.isSuccess && result.getOrNull() != null -> { }
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _bottomSheetState.value = _bottomSheetState.value.copy(error = exception)
          _eventFlow.emit(BottomSheetUiEvent.ShowSnackbar("${exception.message}"))
          Timber.e(exception)
        }
      }
    }
  }

  fun updateBandalartTaskCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartTaskCellModel: UpdateBandalartTaskCellModel,
  ) {
    viewModelScope.launch {
      val result = updateBandalartTaskCellUseCase(bandalartKey, cellKey, updateBandalartTaskCellModel.toEntity())
      when {
        result.isSuccess && result.getOrNull() != null -> { }
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _bottomSheetState.value = _bottomSheetState.value.copy(error = exception)
          _eventFlow.emit(BottomSheetUiEvent.ShowSnackbar("${exception.message}"))
          Timber.e(exception)
        }
      }
    }
  }

  fun deleteBandalartCell(bandalartKey: String, cellKey: String) {
    viewModelScope.launch {
      val result = deleteBandalartCellUseCase(bandalartKey, cellKey)
      when {
        result.isSuccess && result.getOrNull() != null -> { }
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _bottomSheetState.value = _bottomSheetState.value.copy(
            isCellDeleted = false,
            error = exception,
          )
          openDeleteCellDialog(false)
          _eventFlow.emit(BottomSheetUiEvent.ShowSnackbar("${exception.message}"))
          Timber.e(exception)
        }
      }
    }
  }

  fun openDeleteCellDialog(deleteCellDialogState: Boolean) {
    viewModelScope.launch {
      _bottomSheetState.update {
        it.copy(isDeleteCellDialogOpened = deleteCellDialogState)
      }
    }
  }

  fun openDatePicker(datePickerState: Boolean) {
    _bottomSheetState.update {
      it.copy(isDatePickerOpened = datePickerState)
    }
  }

  fun openEmojiPicker(emojiPickerState: Boolean) {
    _bottomSheetState.update {
      it.copy(isEmojiPickerOpened = emojiPickerState)
    }
  }

  fun emojiSelected(profileEmoji: String?) {
    _bottomSheetState.update {
      it.copy(
        cellData = it.cellData.copy(
          profileEmoji = profileEmoji,
        ),
      )
    }
  }

  fun titleChanged(title: String) {
    _bottomSheetState.update {
      it.copy(
        cellData = it.cellData.copy(
          title = title,
        ),
      )
    }
  }

  fun colorChanged(mainColor: String, subColor: String) {
    _bottomSheetState.update {
      it.copy(
        cellData = it.cellData.copy(
          mainColor = mainColor,
          subColor = subColor,
        ),
      )
    }
  }

  fun dueDateChanged(dueDate: String?) {
    _bottomSheetState.update {
      it.copy(
        cellData = it.cellData.copy(
          dueDate = dueDate,
        ),
      )
    }
  }

  fun descriptionChanged(description: String?) {
    _bottomSheetState.update {
      it.copy(
        cellData = it.cellData.copy(
          description = description,
        ),
      )
    }
  }

  fun isCompletedChanged(isCompleted: Boolean) {
    _bottomSheetState.update {
      it.copy(
        cellData = it.cellData.copy(
          isCompleted = isCompleted,
        ),
      )
    }
  }

  fun bottomSheetClosed() {
    _bottomSheetState.update {
      BottomSheetUiState()
    }
  }
}
