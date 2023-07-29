package com.nexters.bandalart.android.feature.home

import com.nexters.bandalart.android.feature.home.model.BandalartMainCellUiModel

sealed interface HomeUiState {
  object Loading : HomeUiState
  data class Success(
    val bandalartData: BandalartMainCellUiModel,
  ) : HomeUiState

  data class Error(val exception: Throwable) : HomeUiState
}
