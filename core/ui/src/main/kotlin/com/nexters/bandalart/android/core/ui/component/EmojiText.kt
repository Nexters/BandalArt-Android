package com.nexters.bandalart.android.core.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.ComponentPreview
import com.nexters.bandalart.android.core.ui.nonScaleSp

@Composable
fun EmojiText(
  emojiText: String?,
  fontSize: TextUnit,
  modifier: Modifier = Modifier,
) {
  Text(
    text = emojiText ?: "",
    modifier = modifier,
    fontSize = fontSize.nonScaleSp,
  )
}

@ComponentPreview
@Composable
fun EmojiTextPreview() {
  EmojiText(
    emojiText = "😎",
    fontSize = 22.sp,
  )
}
