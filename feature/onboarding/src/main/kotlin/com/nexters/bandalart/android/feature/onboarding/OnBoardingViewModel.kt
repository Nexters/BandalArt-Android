package com.nexters.bandalart.android.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.android.core.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface OnBoardingUiEvent {
  data object NavigateToHome : OnBoardingUiEvent
}

@HiltViewModel
class OnboardingViewModel @Inject constructor(
  private val onboardingRepository: OnboardingRepository,
) : ViewModel() {
  private val _uiEvent = Channel<OnBoardingUiEvent>()
  val uiEvent = _uiEvent.receiveAsFlow()

  fun setOnboardingCompletedStatus(flag: Boolean) {
    viewModelScope.launch {
      onboardingRepository.setOnboardingCompletedStatus(flag)
      navigateToHome()
    }
  }

  private fun navigateToHome() {
    viewModelScope.launch {
      _uiEvent.send(OnBoardingUiEvent.NavigateToHome)
    }
  }
}
