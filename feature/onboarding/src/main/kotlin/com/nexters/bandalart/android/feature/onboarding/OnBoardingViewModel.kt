package com.nexters.bandalart.android.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface OnBoardingUiEvent {
  data object NavigateToHome : OnBoardingUiEvent
}

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {
  private val _eventFlow = MutableSharedFlow<OnBoardingUiEvent>()
  val eventFlow: SharedFlow<OnBoardingUiEvent> = _eventFlow.asSharedFlow()

  fun navigateToHome() {
    viewModelScope.launch {
      _eventFlow.emit(OnBoardingUiEvent.NavigateToHome)
    }
  }
}
