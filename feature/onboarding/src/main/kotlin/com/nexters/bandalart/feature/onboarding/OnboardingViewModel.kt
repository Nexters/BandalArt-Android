package com.nexters.bandalart.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.core.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface OnboardingUiEvent {
    data object NavigateToHome : OnboardingUiEvent
}

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {
    private val _uiEvent = Channel<OnboardingUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun setOnboardingCompletedStatus(flag: Boolean) {
        viewModelScope.launch {
            onboardingRepository.setOnboardingCompletedStatus(flag)
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        viewModelScope.launch {
            _uiEvent.send(OnboardingUiEvent.NavigateToHome)
        }
    }
}
