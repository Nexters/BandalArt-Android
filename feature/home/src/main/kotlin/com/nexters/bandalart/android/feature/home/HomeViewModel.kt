package com.nexters.bandalart.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.android.core.domain.usecase.bandalart.CreateBandalartUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.DeleteBandalartUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.GetBandalartDetailUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.GetBandalartListUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.GetBandalartMainCellUseCase
import com.nexters.bandalart.android.feature.home.mapper.toUiModel
import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel
import com.nexters.bandalart.android.feature.home.model.BandalartDetailUiModel
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
 * @param bandalartDetailData 반다라트 상세 데이터, 서버와의 통신을 성공하면 not null
 * @param bandalartChartData 반다라트 표의 데이터, 서버와의 통신을 성공하면 not null
 * @param isCellUpdated 반다라트 표의 특정 셀이 수정됨
 * @param isCellDeleted 반다라트의 표의 특정 셀의 삭제됨(비어있는 셀로 전환)
 * @param isBandalartCompleted 반다라트 표의 메인 목표를 달성함
 * @param isBandalartDeleted 반다라트 표가 삭제됨
 * @param isBandalartCreated 반다라트 표가 생성됨
 * @param isLoading 서버와의 통신 중 로딩 상태
 * @param error 서버와의 통신을 실패
 */

// TODO Token 확인 로직의 위치 결정
data class HomeUiState(
  val bandalartListData: List<BandalartDetailUiModel>? = null,
  val bandalartDetailData: BandalartDetailUiModel? = null,
  val bandalartChartData: BandalartCellUiModel? = null,
  val isCellUpdated: Boolean = false,
  val isCellDeleted: Boolean = false,
  val isBandalartCompleted: Boolean = false,
  val isBandalartDeleted: Boolean = false,
  val isBandalartCreated: Boolean = false,
  val isLoading: Boolean = false,
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
) : ViewModel() {

  private val _uiState = MutableStateFlow(HomeUiState())
  val uiState: StateFlow<HomeUiState> = this._uiState.asStateFlow()

  private val _eventFlow = MutableSharedFlow<HomeUiEvent>()
  val eventFlow: SharedFlow<HomeUiEvent> = _eventFlow.asSharedFlow()

  fun getBandalartList() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true)
      val result = getBandalartListUseCase()
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            bandalartListData = result.getOrNull()!!.map { it.toUiModel() },
            error = null,
          )
        }
        // TODO 해당 케이스의 대한 처리 유무 결정
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            bandalartListData = null,
            error = exception,
          )
          // TODO 에러 메세지 커스텀
          _eventFlow.emit(HomeUiEvent.ShowSnackbar("${exception.message}"))
          Timber.e(exception)
        }
      }
    }
  }

  fun getBandalartDetail(bandalartKey: String) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true)
      val result = getBandalartDetailUseCase(bandalartKey)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          val bandalartDetailData = result.getOrNull()!!.toUiModel()
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            bandalartDetailData = bandalartDetailData,
            error = null,
          )
          getBandalartMainCell(bandalartDetailData.key)
        }
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            bandalartChartData = null,
            error = exception,
          )
          _eventFlow.emit(HomeUiEvent.ShowSnackbar("${exception.message}"))
          Timber.e(exception)
        }
      }
    }
  }

  // TODO 404 인데 Unexpected Error 로 출력 되고 있음
  private fun getBandalartMainCell(bandalartKey: String) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true)
      val result = getBandalartMainCellUseCase(bandalartKey)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            bandalartChartData = result.getOrNull()!!.toUiModel(),
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
            bandalartChartData = null,
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
      _uiState.value = _uiState.value.copy(isLoading = true)
      val result = createBandalartUseCase()
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _uiState.value = _uiState.value.copy(
            isLoading = false,
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
            isLoading = false,
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
      _uiState.value = _uiState.value.copy(isLoading = true)
      val result = deleteBandalartUseCase(bandalartKey)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = null,
          )
          _eventFlow.emit(HomeUiEvent.ShowSnackbar("반다라트가 삭제되었어요."))
        }
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            bandalartChartData = null,
            error = exception,
          )
          _eventFlow.emit(HomeUiEvent.ShowSnackbar("${exception.message}"))
          Timber.e(exception)
        }
      }
    }
  }
}
