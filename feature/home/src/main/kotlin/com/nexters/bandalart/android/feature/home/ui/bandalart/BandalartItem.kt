@file:OptIn(ExperimentalMaterial3Api::class)

package com.nexters.bandalart.android.feature.home.ui.bandalart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.theme.Gray100
import com.nexters.bandalart.android.core.designsystem.theme.Gray300
import com.nexters.bandalart.android.core.designsystem.theme.Gray400
import com.nexters.bandalart.android.core.designsystem.theme.Gray900
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.component.EmojiText
import com.nexters.bandalart.android.core.ui.component.FixedSizeText
import com.nexters.bandalart.android.core.ui.extension.toColor
import com.nexters.bandalart.android.feature.home.model.BandalartDetailUiModel
import kotlinx.coroutines.launch

@Composable
fun BandalartItem(
  bottomSheetState: SheetState,
  bandalartItem: BandalartDetailUiModel,
  currentBandalartKey: String,
  onClick: (String) -> Unit,
  onCancelClicked: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val scope = rememberCoroutineScope()

  Row(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .border(
        width = 1.5.dp,
        color = if (currentBandalartKey != bandalartItem.key) Gray100 else Gray300,
        shape = RoundedCornerShape(12.dp),
      )
      .clickable {
        if (currentBandalartKey != bandalartItem.key) {
          onClick(bandalartItem.key)
        }
        scope
          .launch { bottomSheetState.hide() }
          .invokeOnCompletion {
            if (!bottomSheetState.isVisible) onCancelClicked()
          }
      }
      .padding(horizontal = 16.dp, vertical = 12.dp),
  ) {
    Box(modifier = Modifier.align(Alignment.CenterVertically)) {
      Card(shape = RoundedCornerShape(16.dp)) {
        Box(
          modifier = Modifier
            .width(48.dp)
            .aspectRatio(1f)
            .background(Gray100),
          contentAlignment = Alignment.Center,
        ) {
          if (bandalartItem.profileEmoji.isNullOrEmpty()) {
            Image(
              painter = painterResource(com.nexters.bandalart.android.core.designsystem.R.drawable.ic_empty_emoji),
              contentDescription = stringResource(R.string.empty_emoji_descrption),
            )
          } else {
            EmojiText(
              emojiText = bandalartItem.profileEmoji,
              fontSize = 22.sp,
            )
          }
        }
      }
    }
    Column(
      modifier = Modifier
        .weight(1f)
        .padding(start = 8.dp),
    ) {
      if (bandalartItem.isCompleted) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(color = bandalartItem.mainColor.toColor()),
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = stringResource(R.string.check_descrption),
              tint = Gray900,
              modifier = Modifier.size(13.dp),
            )
            FixedSizeText(
              text = stringResource(R.string.home_complete),
              color = Gray900,
              fontWeight = FontWeight.W600,
              fontSize = 10.sp,
              letterSpacing = (-0.2).sp,
              modifier = Modifier.padding(start = 2.dp),
            )
          }
        }
      } else {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(color = bandalartItem.mainColor.toColor()),
        ) {
          Row(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            FixedSizeText(
              text = stringResource(
                R.string.home_complete_ratio,
                bandalartItem.completionRatio,
              ),
              color = Gray900,
              fontWeight = FontWeight.W600,
              fontSize = 9.sp,
              letterSpacing = (-0.18).sp,
            )
          }
        }
      }
      FixedSizeText(
        text = bandalartItem.title ?: "",
        color = if (bandalartItem.isGeneratedTitle) Gray300 else Gray900,
        fontWeight = FontWeight.W700,
        fontSize = 16.sp,
        letterSpacing = (-0.32).sp,
      )
    }
    if (currentBandalartKey != bandalartItem.key) {
      Box(modifier = Modifier.align(Alignment.CenterVertically)) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
          contentDescription = stringResource(R.string.arrow_forward_descrption),
          tint = Gray400,
          modifier = Modifier.size(16.dp),
        )
      }
    }
  }
}
