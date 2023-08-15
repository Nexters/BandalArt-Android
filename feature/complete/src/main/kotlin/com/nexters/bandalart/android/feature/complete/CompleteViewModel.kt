package com.nexters.bandalart.android.feature.complete

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.android.core.domain.usecase.bandalart.ShareBandalartUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.UpsertBandalartKeyUseCase
import com.nexters.bandalart.android.feature.complete.navigation.BANDALART_KEY
import com.nexters.bandalart.android.feature.complete.navigation.BANDALART_PROFILE_EMOJI
import com.nexters.bandalart.android.feature.complete.navigation.BANDALART_TITLE
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
 * CompleteUiState
 *
 * @param key 반다라트 고유키
 * @param title 반다라트 제목
 * @param profileEmoji 반다라트 프로필 이모지
 * @param shareUrl 공유 링크
 * @param error 서버와의 통신을 실패
 */

data class CompleteUiState(
  val key: String = "",
  val title: String = "",
  val profileEmoji: String? = "",
  val shareUrl: String = "",
  val error: Throwable? = null,
)

sealed class CompleteUiEvent {
  data class ShowSnackbar(val message: String) : CompleteUiEvent()
}

@HiltViewModel
class CompleteViewModel @Inject constructor(
  private val shareBandalartUseCase: ShareBandalartUseCase,
  private val upsertBandalartKeyUseCase: UpsertBandalartKeyUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val key = savedStateHandle[BANDALART_KEY] ?: ""
  private val title = savedStateHandle[BANDALART_TITLE] ?: ""
  private val profileEmoji = savedStateHandle[BANDALART_PROFILE_EMOJI] ?: ""

  private val _uiState = MutableStateFlow(CompleteUiState())
  val uiState: StateFlow<CompleteUiState> = this._uiState.asStateFlow()

  private val _eventFlow = MutableSharedFlow<CompleteUiEvent>()
  val eventFlow: SharedFlow<CompleteUiEvent> = _eventFlow.asSharedFlow()

  init {
    initComplete()
    viewModelScope.launch {
      upsertBandalartKeyUseCase(key, true)
    }
  }

  private fun initComplete() {
    _uiState.value = _uiState.value.copy(
      key = key,
      title = title,
      profileEmoji = profileEmoji,
    )
  }

  fun shareBandalart() {
    viewModelScope.launch {
      val result = shareBandalartUseCase(key)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _uiState.value = _uiState.value.copy(
            shareUrl = result.getOrNull()!!.shareUrl,
            error = null,
          )
        }
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }
        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _uiState.value = _uiState.value.copy(
            error = exception,
          )
          Timber.e(exception)
        }
      }
    }
  }

  fun initShareUrl() {
    _uiState.value = _uiState.value.copy(
      shareUrl = "",
    )
  }
}
