package com.nexters.bandalart.core.common.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.jetbrains.compose.resources.stringResource

sealed class UiText {
    data class DirectString(val value: String) : UiText()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any,
    ) : UiText()

    @Composable
    fun asString() = when (this) {
        is DirectString -> value
        is StringResource -> stringResource(resId, *args)
    }

    fun asString(context: Context) = when (this) {
        is DirectString -> value
        is StringResource -> context.getString(resId, *args)
    }
}
