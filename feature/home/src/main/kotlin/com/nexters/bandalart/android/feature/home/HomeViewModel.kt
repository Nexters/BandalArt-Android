package com.nexters.bandalart.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.bandalart.android.core.domain.usecase.GetBandalartMainCellUseCase
import com.nexters.bandalart.android.feature.home.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getBandalartMainCellUseCase: GetBandalartMainCellUseCase,
) : ViewModel() {

  private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
  val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

  fun getBandalartMainCell(bandalartKey: String) {
    viewModelScope.launch {
      _homeUiState.value = HomeUiState.Loading
      val result = getBandalartMainCellUseCase(bandalartKey)
      when {
        result.isSuccess && result.getOrNull() != null -> {
          _homeUiState.value = HomeUiState.Success(result.getOrNull()!!.toUiModel())
        }
        result.isSuccess && result.getOrNull() == null -> {
          Timber.e("Request succeeded but data validation failed")
        }
        result.isFailure -> {
          _homeUiState.value = HomeUiState.Error(result.exceptionOrNull()!!)
          Timber.e(result.exceptionOrNull())
        }
      }
    }
  }
}
