package com.nexters.bandalart.android.feature.home

import com.nexters.bandalart.android.feature.home.model.BandalartCellUiModel

sealed interface HomeUiState {
  object Loading : HomeUiState
  data class Success(
    val bandalartData: BandalartCellUiModel,
  ) : HomeUiState

  data class Error(val exception: Throwable) : HomeUiState
}
