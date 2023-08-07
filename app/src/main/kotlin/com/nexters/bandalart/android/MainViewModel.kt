package com.nexters.bandalart.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.android.core.domain.usecase.login.CreateGuestLoginTokenUseCase
import com.nexters.bandalart.android.core.domain.usecase.login.GetGuestLoginTokenUseCase
import com.nexters.bandalart.android.core.domain.usecase.login.SetGuestLoginTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * MainUiState
 *
 * @param isLoggedIn 로그인 완료
 * @param isLoading 서버와의 통신 중 로딩 상태
 * @param error 서버와의 통신을 실패
 */

data class MainUiState(
  val isLoggedIn: Boolean = false,
  val isLoading: Boolean = true,
  val error: Throwable? = null,
)

@HiltViewModel
class MainViewModel @Inject constructor(
  private val getGuestLoginTokenUseCase: GetGuestLoginTokenUseCase,
  private val createGuestLoginTokenUseCase: CreateGuestLoginTokenUseCase,
  private val setGuestLoginTokenUseCase: SetGuestLoginTokenUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(MainUiState())
  val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

  init {
    getGuestLoginToken()
  }

  private fun getGuestLoginToken() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(
        isLoading = true,
      )
      val guestLoginToken = getGuestLoginTokenUseCase()
      Timber.d(guestLoginToken)
      if (guestLoginToken.isEmpty()) {
        val result = createGuestLoginTokenUseCase()
        when {
          result.isSuccess && result.getOrNull() != null -> {
            val newGuestLoginToken = result.getOrNull()!!
            setGuestLoginTokenUseCase(newGuestLoginToken.key)
            _uiState.value = _uiState.value.copy(
              isLoggedIn = false,
              isLoading = false,
            )
          }
          result.isSuccess && result.getOrNull() == null -> {
            Timber.e("Request succeeded but data validation failed")
          }
          result.isFailure -> {
            val exception = result.exceptionOrNull()
            _uiState.value = _uiState.value.copy(
              isLoggedIn = false,
              isLoading = false,
            )
            Timber.e(exception)
          }
        }
      } else {
        _uiState.value = _uiState.value.copy(
          isLoggedIn = true,
          isLoading = false,
        )
      }
    }
  }
}
