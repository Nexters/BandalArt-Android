package com.nexters.bandalart.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.android.core.domain.usecase.bandalart.DeleteBandalartUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.GetBandalartMainCellUseCase
import com.nexters.bandalart.android.feature.home.mapper.toUiModel
import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel
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

// TODO Token 확인 로직의 위치 결정
data class HomeUiState(
  val bandalartData: BandalartCellUiModel? = null,
  val isCellUpdated: Boolean = false,
  val isCellDeleted: Boolean = false,
  val isBandalartCompleted: Boolean = false,
  val isBandalartDeleted: Boolean = false,
  val isLoading: Boolean = false,
  val error: Throwable? = null,
)

sealed class HomeUiEvent {
  data class ShowSnackbar(val message: String) : HomeUiEvent()
}

@Suppress("unused")
@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getBandalartMainCellUseCase: GetBandalartMainCellUseCase,
  private val deleteBandalartUseCase: DeleteBandalartUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(HomeUiState())
  val uiState: StateFlow<HomeUiState> = this._uiState.asStateFlow()

  private val _eventFlow = MutableSharedFlow<HomeUiEvent>()
  val eventFlow: SharedFlow<HomeUiEvent> = _eventFlow.asSharedFlow()

  fun getBandalartMainCell(bandalartKey: String) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true)
      val result = getBandalartMainCellUseCase(bandalartKey)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            bandalartData = result.getOrNull()!!.toUiModel(),
            error = null,
          )
        }
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            bandalartData = null,
            error = exception,
          )
          // TODO 에러 메세지 커스텀
          _eventFlow.emit(HomeUiEvent.ShowSnackbar("${exception.message}"))
          Timber.e(exception)
        }
      }
    }
  }
}
