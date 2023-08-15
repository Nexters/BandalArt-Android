package com.nexters.bandalart.android.core.ui.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val TextUnit.nonScaleSp
  @Composable
  get() = if (this == TextUnit.Unspecified) this
  else (this.value / LocalDensity.current.fontScale).sp
