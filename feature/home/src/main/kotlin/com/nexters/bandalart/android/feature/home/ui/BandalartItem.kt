@file:OptIn(ExperimentalMaterial3Api::class)

package com.nexters.bandalart.android.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.R
import com.nexters.bandalart.android.core.ui.component.EmojiText
import com.nexters.bandalart.android.core.ui.component.FixedSizeText
import com.nexters.bandalart.android.core.ui.extension.toColor
import com.nexters.bandalart.android.core.ui.theme.Gray100
import com.nexters.bandalart.android.core.ui.theme.Gray400
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.pretendard
import com.nexters.bandalart.android.feature.home.model.BandalartDetailUiModel
import kotlinx.coroutines.launch

@Composable
fun BandalartItem(
  modifier: Modifier = Modifier,
  bottomSheetState: SheetState,
  bandalartItem: BandalartDetailUiModel,
  currentBandalartKey: String,
  onClick: (String) -> Unit,
  onCancelClicked: () -> Unit,
) {
  val scope = rememberCoroutineScope()

  Row(
    modifier = modifier
      .clickable {
        if (currentBandalartKey != bandalartItem.key) {
          onClick(bandalartItem.key)
        } else {
          scope.launch { bottomSheetState.hide() }
            .invokeOnCompletion {
              if (!bottomSheetState.isVisible) onCancelClicked()
            }
        }
      },
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
            val image = painterResource(id = R.drawable.ic_empty_emoji)
            Image(
              painter = image,
              contentDescription = "Empty Emoji Icon",
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
            modifier = Modifier.padding(start = 9.dp, end = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = "Check Icon",
              tint = Gray900,
              modifier = Modifier.size(13.dp),
            )
            FixedSizeText(
              text = "달성 완료!",
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
            // TODO 데이터 연동
            FixedSizeText(
              text = "달성률 (75%)",
              // text = "달성률 (${bandalartItem.completionRatio}%)",
              color = Gray900,
              fontWeight = FontWeight.W600,
              fontSize = 9.sp,
              letterSpacing = (-0.18).sp,
            )
          }
        }
      }
      Text(
        text = bandalartItem.title ?: "",
        color = Gray900,
        fontFamily = pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 16.sp,
        letterSpacing = (-0.32).sp,
      )
    }
    if (!bandalartItem.isCompleted) {
      Box(modifier = Modifier.align(Alignment.CenterVertically)) {
        Icon(
          imageVector = Icons.Default.ArrowForwardIos,
          contentDescription = "Arrow Foward Icon",
          tint = Gray400,
          modifier = Modifier.size(16.dp),
        )
      }
    }
  }
}
