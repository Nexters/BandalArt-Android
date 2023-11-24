package com.nexters.bandalart.android.core.ui.extension

import android.annotation.SuppressLint
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import com.nexters.bandalart.android.core.ui.MultipleEventsCutter
import com.nexters.bandalart.android.core.ui.get

@SuppressLint("ModifierFactoryUnreferencedReceiver")
inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
  clickable(
    indication = null,
    interactionSource = remember { MutableInteractionSource() },
  ) {
    onClick()
  }
}

fun Modifier.clickableSingle(
  enabled: Boolean = true,
  onClickLabel: String? = null,
  role: Role? = null,
  onClick: () -> Unit,
) = composed(
  inspectorInfo = debugInspectorInfo {
    name = "clickable"
    properties["enabled"] = enabled
    properties["onClickLabel"] = onClickLabel
    properties["role"] = role
    properties["onClick"] = onClick
  },
) {
  val multipleEventsCutter = remember { MultipleEventsCutter.get() }
  Modifier.clickable(
    enabled = enabled,
    onClickLabel = onClickLabel,
    onClick = { multipleEventsCutter.processEvent { onClick() } },
    role = role,
    indication = LocalIndication.current,
    interactionSource = remember { MutableInteractionSource() },
  )
}
