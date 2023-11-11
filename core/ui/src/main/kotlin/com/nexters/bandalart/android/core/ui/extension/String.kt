package com.nexters.bandalart.android.core.ui.extension

import androidx.compose.ui.graphics.Color

fun String.toColor(): Color {
  return Color(android.graphics.Color.parseColor(this))
}
