package com.nexters.bandalart.core.ui

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.StateFlow

@Stable
interface UiState

@Stable
interface UiEvent

@Stable
interface UiAction

inline fun <reified S : UiState> StateFlow<UiState>.checkState(action: S.() -> Unit) {
    (this.value as? S)?.let(action)
}
