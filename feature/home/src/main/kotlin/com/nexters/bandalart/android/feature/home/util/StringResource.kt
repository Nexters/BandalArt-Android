package com.nexters.bandalart.android.feature.home.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class StringResource {

  data class ViewModelStringViewModel(val value: String) : StringResource()
  class StringResourceText(@StringRes val resId: Int) : StringResource()

  @Composable
  fun asString() = when (this) {
    is StringResourceText -> stringResource(resId)
    is ViewModelStringViewModel -> value
  }

  fun asString(context: Context) = when (this) {
    is StringResourceText -> context.getString(resId)
    is ViewModelStringViewModel -> value
  }
}
