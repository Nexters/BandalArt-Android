package com.nexters.bandalart.android.feature.complete

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.android.core.common.ErrorHandlerActions
import com.nexters.bandalart.android.core.common.UiText
import com.nexters.bandalart.android.core.domain.usecase.bandalart.ShareBandalartUseCase
import com.nexters.bandalart.android.core.domain.usecase.bandalart.UpsertBandalartKeyUseCase
import com.nexters.bandalart.android.feature.complete.navigation.BANDALART_KEY
import com.nexters.bandalart.android.feature.complete.navigation.BANDALART_PROFILE_EMOJI
import com.nexters.bandalart.android.feature.complete.navigation.BANDALART_TITLE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * CompleteUiState
 *
 * @param key 반다라트 고유키
 * @param title 반다라트 제목
 * @param profileEmoji 반다라트 프로필 이모지
 * @param shareUrl 공유 링크
 */

data class CompleteUiState(
  val key: String = "",
  val title: String = "",
  val profileEmoji: String = "",
  val shareUrl: String = "",
  val isNetworkErrorDialogVisible: Boolean = false,
  val isServerErrorDialogVisible: Boolean = false,
)

sealed interface CompleteUiEvent {
  data object NavigateToHome : CompleteUiEvent
  data class ShowToast(val message: UiText) : CompleteUiEvent
}

@HiltViewModel
class CompleteViewModel @Inject constructor(
  private val shareBandalartUseCase: ShareBandalartUseCase,
  private val upsertBandalartKeyUseCase: UpsertBandalartKeyUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel(), ErrorHandlerActions {
  private var shareBandalartJob: Job? = null

  private val key = savedStateHandle[BANDALART_KEY] ?: ""
  private val title = savedStateHandle[BANDALART_TITLE] ?: ""
  private val profileEmoji = savedStateHandle[BANDALART_PROFILE_EMOJI] ?: ""

  private val _uiState = MutableStateFlow(CompleteUiState())
  val uiState: StateFlow<CompleteUiState> = this._uiState.asStateFlow()

  private val _eventChannel = Channel<CompleteUiEvent>()
  val eventFlow = _eventChannel.receiveAsFlow()

  init {
    initComplete()
    viewModelScope.launch {
      upsertBandalartKeyUseCase(
        bandalartKey = key,
        isCompleted = true,
      )
    }
  }

  private fun initComplete() {
    _uiState.update {
      it.copy(
        key = key,
        title = title,
        profileEmoji = profileEmoji,
      )
    }
  }

  fun shareBandalart() {
    if (shareBandalartJob?.isActive == true && _uiState.value.shareUrl.isNotEmpty()) {
      return
    }
    shareBandalartJob = viewModelScope.launch {
      val result = shareBandalartUseCase(key)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _uiState.value = _uiState.value.copy(shareUrl = result.getOrNull()!!.shareUrl)
        }

        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }

        result.isFailure -> {
          val exception = result.exceptionOrNull()!!
          _eventChannel.send(CompleteUiEvent.ShowToast(UiText.DirectString(exception.message.toString())))
          Timber.e(exception)
        }
      }
      shareBandalartJob = null
    }
  }

  fun initShareUrl() {
    _uiState.value = _uiState.value.copy(shareUrl = "")
  }

  fun navigateToHome() {
    viewModelScope.launch {
      _eventChannel.send(CompleteUiEvent.NavigateToHome)
    }
  }

  override fun setNetworkErrorDialogVisible(flag: Boolean) {
    _uiState.update { it.copy(isNetworkErrorDialogVisible = flag) }
  }

  override fun setServerErrorDialogVisible(flag: Boolean) {
    _uiState.update { it.copy(isServerErrorDialogVisible = flag) }
  }
}
