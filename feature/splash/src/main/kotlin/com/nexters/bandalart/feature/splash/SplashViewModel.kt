package com.nexters.bandalart.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.core.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SplashUiEvent {
    data object NavigateToOnBoarding : SplashUiEvent
    data object NavigateToHome : SplashUiEvent
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
    private val bandalartRepository: BandalartRepository,
) : ViewModel() {
    private val _uiEvent = Channel<SplashUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            getOnboardingCompletedStatus()
        }
    }

    private fun getOnboardingCompletedStatus() {
        viewModelScope.launch {
            val onboardingCompletedStatus = onboardingRepository.getOnboardingCompletedStatus()
            if (onboardingCompletedStatus) {
                _uiEvent.send(SplashUiEvent.NavigateToHome)
            } else {
                bandalartRepository.createBandalart()
                _uiEvent.send(SplashUiEvent.NavigateToOnBoarding)
            }
        }
    }
}
