package com.nexters.bandalart.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.android.core.domain.usecase.CheckServerHealthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val checkServerHealthUseCase: CheckServerHealthUseCase,
) : ViewModel() {

  fun checkServerHealth() {
    viewModelScope.launch {
      checkServerHealthUseCase()
    }
  }
}
