package com.nexters.bandalart.android.core.ui

import android.app.Activity
import android.graphics.Rect
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity

// https://sungbin.land/jetpack-compose-windowinsets-fa8f286f092b
val NavigationBarHeightDp
  @Composable
  get() = with(LocalDensity.current) {
    WindowInsets.systemBars.getBottom(this).toDp()
  }

val StatusBarAndActionBarHeightDp
  @Composable
  get() = with(LocalDensity.current) {
    WindowInsets.systemBars.getTop(this).toDp()
  }

val StatusBarHeightDp
  @Composable
  get() = with(LocalContext.current) {
    val activity = this as Activity

    val rectangle = Rect()
    activity.window.decorView.getWindowVisibleDisplayFrame(rectangle)

    with(LocalDensity.current) {
      rectangle.top.toDp()
    }
  }
