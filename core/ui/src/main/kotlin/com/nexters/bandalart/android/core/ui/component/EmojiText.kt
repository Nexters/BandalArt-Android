package com.nexters.bandalart.android.core.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp

@Composable
fun EmojiText(
  emojiText: String,
  fontSize: TextUnit,
) {
  Text(
    text = emojiText,
    fontSize = fontSize.nonScaleSp,
  )
}
