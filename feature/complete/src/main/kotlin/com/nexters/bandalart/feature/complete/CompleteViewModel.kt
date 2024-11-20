package com.nexters.bandalart.feature.complete

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.core.navigation.Route
import com.nexters.bandalart.feature.complete.viewmodel.CompleteUiAction
import com.nexters.bandalart.feature.complete.viewmodel.CompleteUiEvent
import com.nexters.bandalart.feature.complete.viewmodel.CompleteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteViewModel @Inject constructor(
    private val bandalartRepository: BandalartRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val id = savedStateHandle.toRoute<Route.Complete>().bandalartId
    private val title = savedStateHandle.toRoute<Route.Complete>().bandalartTitle
    private val profileEmoji = savedStateHandle.toRoute<Route.Complete>().bandalartProfileEmoji ?: ""

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

    fun onAction(action: CompleteUiAction) {
        when (action) {
            is CompleteUiAction.OnShareButtonClick -> shareBandalart()
            is CompleteUiAction.OnBackButtonClick -> navigateBack()
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

    private fun shareBandalart() {}

    private fun navigateBack() {
        viewModelScope.launch {
            _uiEvent.send(CompleteUiEvent.NavigateBack)
        }
    }
}
