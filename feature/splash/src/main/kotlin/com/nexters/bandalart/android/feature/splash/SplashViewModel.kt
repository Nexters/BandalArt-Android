package com.nexters.bandalart.android.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.android.core.domain.usecase.bandalart.CreateBandalartUseCase
import com.nexters.bandalart.android.core.domain.usecase.login.CreateGuestLoginTokenUseCase
import com.nexters.bandalart.android.core.domain.usecase.login.GetGuestLoginTokenUseCase
import com.nexters.bandalart.android.core.domain.usecase.login.SetGuestLoginTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * SplashUiState
 *
 * @param isLoggedIn 로그인 여부 확인
 * @param isNetworkErrorAlertDialogOpened 네트워크 에러 발생
 * @param isLoading 서버와의 통신 중 로딩 상태
 */

data class SplashUiState(
  val isLoggedIn: Boolean = false,
  val isNetworkErrorAlertDialogOpened: Boolean = false,
  val isLoading: Boolean = true,
)

sealed interface SplashUiEvent {
  data object NavigateToOnBoarding : SplashUiEvent
  data object NavigateToHome : SplashUiEvent
}

@HiltViewModel
class SplashViewModel @Inject constructor(
  private val getGuestLoginTokenUseCase: GetGuestLoginTokenUseCase,
  private val createGuestLoginTokenUseCase: CreateGuestLoginTokenUseCase,
  private val setGuestLoginTokenUseCase: SetGuestLoginTokenUseCase,
  private val createBandalartUseCase: CreateBandalartUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(SplashUiState())
  val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

  private val _eventFlow = MutableSharedFlow<SplashUiEvent>()
  val eventFlow: SharedFlow<SplashUiEvent> = _eventFlow.asSharedFlow()

  init {
    viewModelScope.launch {
      delay(500)
      getGuestLoginToken()
    }
  }

  private fun getGuestLoginToken() {
    viewModelScope.launch {
      val guestLoginToken = getGuestLoginTokenUseCase()
      if (guestLoginToken.isEmpty()) {
        createGuestLoginToken()
      } else {
        _uiState.update {
          it.copy(
            isLoggedIn = true,
            isLoading = false,
          )
        }
      }
    }
  }

  fun createGuestLoginToken() {
    viewModelScope.launch {
      val delayJob = launch {
        delay(500)
        _uiState.update { it.copy(isLoading = true) }
      }
      val result = createGuestLoginTokenUseCase()
      delayJob.cancel()
      when {
        result.isSuccess && result.getOrNull() != null -> {
          val newGuestLoginToken = result.getOrNull()!!
          setGuestLoginTokenUseCase(newGuestLoginToken.key)
          _uiState.update { it.copy(isLoggedIn = false) }
          createBandalartUseCase()
        }

        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }

        result.isFailure -> {
          _uiState.update { it.copy(isNetworkErrorAlertDialogOpened = true) }
        }
      }
      _uiState.update { it.copy(isLoading = false) }
    }
  }

  fun openNetworkErrorAlertDialog(flag: Boolean) {
    _uiState.update { it.copy(isNetworkErrorAlertDialogOpened = flag) }
  }

  fun navigateToOnBoarding() {
    viewModelScope.launch {
      _eventFlow.emit(SplashUiEvent.NavigateToOnBoarding)
    }
  }

  fun navigateToHome() {
    viewModelScope.launch {
      _eventFlow.emit(SplashUiEvent.NavigateToHome)
    }
  }
}
