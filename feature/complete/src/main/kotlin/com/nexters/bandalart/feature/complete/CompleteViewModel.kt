package com.nexters.bandalart.feature.complete

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.core.common.UiText
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.feature.complete.navigation.BANDALART_ID
import com.nexters.bandalart.feature.complete.navigation.BANDALART_PROFILE_EMOJI
import com.nexters.bandalart.feature.complete.navigation.BANDALART_TITLE
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
 * CompleteUiState
 *
 * @param id 반다라트 고유 id
 * @param title 반다라트 제목
 * @param profileEmoji 반다라트 프로필 이모지
 * @param shareUrl 공유 링크
 */
data class CompleteUiState(
    val id: Long = 0L,
    val title: String = "",
    val profileEmoji: String = "",
    val shareUrl: String = "",
)

sealed interface CompleteUiEvent {
    data object NavigateToHome : CompleteUiEvent
}

@HiltViewModel
class CompleteViewModel @Inject constructor(
    private val bandalartRepository: BandalartRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val id = savedStateHandle[BANDALART_ID] ?: 0L
    private val title = savedStateHandle[BANDALART_TITLE] ?: ""
    private val profileEmoji = savedStateHandle[BANDALART_PROFILE_EMOJI] ?: ""

    private val _uiState = MutableStateFlow(CompleteUiState())
    val uiState: StateFlow<CompleteUiState> = this._uiState.asStateFlow()

    private val _uiEvent = Channel<CompleteUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        initComplete()
        viewModelScope.launch {
            bandalartRepository.upsertBandalartId(
                bandalartId = id,
                isCompleted = true,
            )
        }
    }

    private fun initComplete() {
        _uiState.update {
            it.copy(
                id = id,
                title = title,
                profileEmoji = profileEmoji,
            )
        }
    }

    fun shareBandalart() {}

    fun navigateToHome() {
        viewModelScope.launch {
            _uiEvent.send(CompleteUiEvent.NavigateToHome)
        }
    }
}
