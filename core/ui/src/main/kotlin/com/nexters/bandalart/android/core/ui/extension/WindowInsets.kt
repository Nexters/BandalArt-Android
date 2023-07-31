package com.nexters.bandalart.android.core.ui.extension

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity

// https://sungbin.land/jetpack-compose-windowinsets-fa8f286f092b
val NavigationBarHeightDp
  @Composable
  get() = with(LocalDensity.current) {
    WindowInsets.systemBars.getBottom(this).toDp()
  }

val StatusBarHeightDp
  @Composable
  get() = with(LocalDensity.current) {
    WindowInsets.systemBars.getTop(this).toDp()
  }
