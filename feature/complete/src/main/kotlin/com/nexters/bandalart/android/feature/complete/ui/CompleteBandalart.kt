package com.nexters.bandalart.android.feature.complete.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.theme.Black
import com.nexters.bandalart.android.core.designsystem.theme.Gray100
import com.nexters.bandalart.android.core.designsystem.theme.Gray300
import com.nexters.bandalart.android.core.designsystem.theme.Gray400
import com.nexters.bandalart.android.core.ui.ComponentPreview
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.component.EmojiText
import com.nexters.bandalart.android.core.ui.component.FixedSizeText

@Composable
fun CompleteBandalart(
  profileEmoji: String,
  title: String,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current

  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    FixedSizeText(
      text = context.getString(R.string.complete_chart),
      color = Gray400,
      fontWeight = FontWeight.W600,
      fontSize = 14.sp,
      letterSpacing = (-0.28).sp,
    )
    Spacer(modifier = Modifier.height(10.dp))
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 33.dp)
        .clip(RoundedCornerShape(12.dp))
        .border(
          width = 2.dp,
          color = Gray300,
          shape = RoundedCornerShape(12.dp),
        ),
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Spacer(modifier = Modifier.height(16.dp))
        Card(
          shape = RoundedCornerShape(12.dp),
          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
          Column {
            Box(
              modifier = Modifier
                .width(52.dp)
                .aspectRatio(1f)
                .background(Gray100),
              contentAlignment = Alignment.Center,
            ) {
              if (profileEmoji == context.getString(R.string.home_default_emoji)) {
                Image(
                  imageVector = ImageVector.vectorResource(
                    id = com.nexters.bandalart.android.core.designsystem.R.drawable.ic_empty_emoji,
                  ),
                  contentDescription = context.getString(R.string.empty_emoji_descrption),
                )
              } else {
                EmojiText(
                  emojiText = profileEmoji,
                  fontSize = 22.sp,
                )
              }
            }
          }
        }
        Spacer(modifier = Modifier.height(6.dp))
        FixedSizeText(
          text = title,
          color = Black,
          fontSize = 16.sp,
          fontWeight = FontWeight.W700,
          letterSpacing = (-0.32).sp,
        )
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

@ComponentPreview
@Composable
fun CompleteBandalartPreview() {
  CompleteBandalart(
    profileEmoji = "😎",
    title = "발전하는 예진",
  )
}
