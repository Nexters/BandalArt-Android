package com.nexters.bandalart.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.android.core.domain.usecase.bandalart.CreateBandalartUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.DeleteBandalartUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.GetBandalartDetailUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.GetBandalartListUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.GetBandalartMainCellUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.UpdateBandalartMainCellUseCase
import com.nexters.bandalart.android.feature.home.mapper.toEntity
import com.nexters.bandalart.android.feature.home.mapper.toUiModel
import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.android.feature.home.model.BandalartDetailUiModel
import com.nexters.bandalart.android.feature.home.model.UpdateBandalartMainCellModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * HomeUiState
 *
 * @param bandalartList 반다라트 목록
 * @param bandalartDetailData 반다라트 상세 데이터, 서버와의 통신을 성공하면 not null
 * @param bandalartCellData 반다라트 표의 데이터, 서버와의 통신을 성공하면 not null
 * @param isBandalartCompleted 반다라트 표의 메인 목표를 달성함
 * @param isBandalartCreated 반다라트 표가 생성됨
 * @param isBandalartDeleted 반다라트 표가 삭제됨
 * @param isDropDownMenuOpened 드롭다운메뉴가 열림
 * @param isBandalartDeleteAlertDialogOpened 반다라트 표 삭제 다이얼로그가 열림
 * @param isBottomSheetDataChanged 바텀시트의 데이터가 변경됨
 * @param isBottomSheetMainCellChanged 바텀시트의 변경된 데이터가 메인 셀임
 * @param isTopLoading 상단바가 서버와의 통신 중 로딩 상태
 * @param isBottomLoading 표가 서버와의 통신 중 로딩 상태
 * @param error 서버와의 통신을 실패
 */

data class HomeUiState(
  val bandalartList: List<BandalartDetailUiModel> = emptyList(),
  val bandalartDetailData: BandalartDetailUiModel? = null,
  val bandalartCellData: BandalartCellUiModel? = null,
  val isBandalartCompleted: Boolean = false,
  val isBandalartCreated: Boolean = false,
  val isBandalartDeleted: Boolean = false,
  val isDropDownMenuOpened: Boolean = false,
  val isBandalartDeleteAlertDialogOpened: Boolean = false,
  val isBottomSheetDataChanged: Boolean = false,
  val isBottomSheetMainCellChanged: Boolean = false,
  val isTopLoading: Boolean = true,
  val isBottomLoading: Boolean = true,
  val error: Throwable? = null,
)

sealed class HomeUiEvent {
  data class ShowSnackbar(val message: String) : HomeUiEvent()
}

@Suppress("unused")
@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getBandalartListUseCase: GetBandalartListUseCase,
  private val getBandalartDetailUseCase: GetBandalartDetailUseCase,
  private val getBandalartMainCellUseCase: GetBandalartMainCellUseCase,
  private val createBandalartUseCase: CreateBandalartUseCase,
  private val deleteBandalartUseCase: DeleteBandalartUseCase,
  private val updateBandalartMainCellUseCase: UpdateBandalartMainCellUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(HomeUiState())
  val uiState: StateFlow<HomeUiState> = this._uiState.asStateFlow()

  private val _eventFlow = MutableSharedFlow<HomeUiEvent>()
  val eventFlow: SharedFlow<HomeUiEvent> = _eventFlow.asSharedFlow()

  init {
    _uiState.value = _uiState.value.copy(
      isTopLoading = true,
      isBottomLoading = true,
    )
  }
  fun getBandalartList() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(
        isTopLoading = true,
        isBottomLoading = true,
      )
      val result = getBandalartListUseCase()
      when {
        result.isSuccess && result.getOrNull() != null -> {
          val bandalartList = result.getOrNull()!!.map { it.toUiModel() }
          _uiState.value = _uiState.value.copy(
            bandalartList = bandalartList,
            error = null,
          )
          getBandalartDetail(bandalartList[0].key)
        }
        // TODO 해당 케이스의 대한 처리 유무 결정
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            isTopLoading = false,
            isBottomLoading = false,
            bandalartList = emptyList(),
            error = exception,
          )
          _eventFlow.emit(HomeUiEvent.ShowSnackbar("${exception.message}"))
          Timber.e(exception)
        }
      }
    }
  }

  fun getBandalartDetail(bandalartKey: String) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(
        isTopLoading = if (_uiState.value.isBottomSheetMainCellChanged) true else _uiState.value.isTopLoading,
        isBottomLoading = true,
      )
      val result = getBandalartDetailUseCase(bandalartKey)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          val bandalartDetailData = result.getOrNull()!!.toUiModel()
          _uiState.value = _uiState.value.copy(
            bandalartDetailData = bandalartDetailData,
            error = null,
          )
          getBandalartMainCell(bandalartKey)
        }
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            isTopLoading = false,
            isBottomLoading = false,
            bandalartCellData = null,
            error = exception,
          )
          _eventFlow.emit(HomeUiEvent.ShowSnackbar("${exception.message}"))
          Timber.e(exception)
        }
      }
    }
  }

  private fun getBandalartMainCell(bandalartKey: String) {
    viewModelScope.launch {
      val result = getBandalartMainCellUseCase(bandalartKey)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _uiState.value = _uiState.value.copy(
            isTopLoading = false,
            isBottomLoading = false,
            bandalartCellData = result.getOrNull()!!.toUiModel(),
            error = null,
          )
          bottomSheetDataChanged(isBottomSheetDataChangedState = false)
          bottomSheetMainCellChanged(isBottomSheetMainCellChangedState = false)
        }
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            isTopLoading = false,
            isBottomLoading = false,
            bandalartCellData = null,
            error = exception,
          )
          _eventFlow.emit(HomeUiEvent.ShowSnackbar("${exception.message}"))
          Timber.e(exception)
        }
      }
    }
  }

  fun createBandalart() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(
        isTopLoading = true,
        isBottomLoading = true,
      )
      val result = createBandalartUseCase()
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _uiState.value = _uiState.value.copy(
            isTopLoading = false,
            isBottomLoading = false,
            error = null,
          )
          // 임시 이벤트 성공 처리
          // TODO 표가 뒤집히는 애니메이션 구현
          _eventFlow.emit(HomeUiEvent.ShowSnackbar("새로운 반다라트를 생성했어요."))
        }
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            isTopLoading = false,
            isBottomLoading = false,
            error = exception,
          )
          _eventFlow.emit(HomeUiEvent.ShowSnackbar("${exception.message}"))
          Timber.e(exception)
        }
      }
    }
  }

  fun deleteBandalart(bandalartKey: String) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(
        isTopLoading = true,
        isBottomLoading = true,
      )
      val result = deleteBandalartUseCase(bandalartKey)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _uiState.value = _uiState.value.copy(
            isTopLoading = false,
            isBottomLoading = false,
            isBandalartDeleted = true,
            error = null,
          )
          openBandalartDeleteAlertDialog(false)
          _eventFlow.emit(HomeUiEvent.ShowSnackbar("반다라트가 삭제되었어요."))
        }
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            isTopLoading = false,
            isBottomLoading = false,
            isBandalartDeleted = false,
            error = exception,
          )
          openBandalartDeleteAlertDialog(false)
          _eventFlow.emit(HomeUiEvent.ShowSnackbar("${exception.message}"))
          Timber.e(exception)
        }
      }
    }
  }

  fun updateBandalartMainCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartMainCellModel: UpdateBandalartMainCellModel,
  ) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(
        isTopLoading = true,
        isBottomLoading = true,
      )
      val result = updateBandalartMainCellUseCase(bandalartKey, cellKey, updateBandalartMainCellModel.toEntity())
      when {
        result.isSuccess && result.getOrNull() != null -> {
          getBandalartDetail(bandalartKey)
        }
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            isTopLoading = false,
            isBottomLoading = false,
            error = exception,
          )
          _eventFlow.emit(HomeUiEvent.ShowSnackbar("${exception.message}"))
          Timber.e(exception)
        }
      }
    }
  }

  fun openDropDownMenu(state: Boolean) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(
        isDropDownMenuOpened = state,
      )
    }
  }

  fun openBandalartDeleteAlertDialog(state: Boolean) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(
        isBandalartDeleteAlertDialogOpened = state,
      )
    }
  }

  fun bottomSheetDataChanged(isBottomSheetDataChangedState: Boolean) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(
        isBottomSheetDataChanged = isBottomSheetDataChangedState,
      )
    }
  }

  fun bottomSheetMainCellChanged(isBottomSheetMainCellChangedState: Boolean) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(
        isBottomSheetMainCellChanged = isBottomSheetMainCellChangedState,
      )
    }
  }
}
