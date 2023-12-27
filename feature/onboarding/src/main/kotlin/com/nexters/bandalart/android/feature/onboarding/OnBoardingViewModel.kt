package com.nexters.bandalart.android.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.android.core.domain.usecase.bandalart.SetOnboardingCompletedStatusUseCase
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
class OnboardingViewModel @Inject constructor(
  private val setOnboardingCompletedStatusUseCase: SetOnboardingCompletedStatusUseCase,
) : ViewModel() {
  private val _eventFlow = MutableSharedFlow<OnBoardingUiEvent>()
  val eventFlow: SharedFlow<OnBoardingUiEvent> = _eventFlow.asSharedFlow()

  fun setOnboardingCompletedStatus(flag: Boolean) {
    viewModelScope.launch {
      setOnboardingCompletedStatusUseCase(flag)
      navigateToHome()
    }
  }

  private fun navigateToHome() {
    viewModelScope.launch {
      _eventFlow.emit(OnBoardingUiEvent.NavigateToHome)
    }
  }
}
