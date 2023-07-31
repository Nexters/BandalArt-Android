package com.nexters.bandalart.android.feature.complete

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.R
import com.nexters.bandalart.android.core.ui.component.BandalartButton
import com.nexters.bandalart.android.core.ui.component.EmojiText
import com.nexters.bandalart.android.core.ui.component.FixedSizeText
import com.nexters.bandalart.android.core.ui.component.TitleText
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.theme.Gray100
import com.nexters.bandalart.android.core.ui.theme.Gray300
import com.nexters.bandalart.android.core.ui.theme.Gray400
import com.nexters.bandalart.android.core.ui.theme.Gray900
import com.nexters.bandalart.android.core.ui.theme.pretendard

@Composable
internal fun CompleteRoute(
  onNavigateBack: () -> Unit,
) {
  CompleteScreen(
    onNavigateBack = onNavigateBack,
  )
}

@Composable
internal fun CompleteScreen(
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier.fillMaxSize(),
  ) {
    Box {
      Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
          Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        ) {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
              .width(32.dp)
              .aspectRatio(1f),
          ) {
            Icon(
              imageVector = Icons.Default.ArrowBackIos,
              contentDescription = "Arrow Back Icon",
              tint = Gray900,
            )
          }
        }
        Spacer(modifier = Modifier.height(40.dp))
        TitleText(text = "반다라트의 모든 목표를 달성했어요.\n정말 대단해요!")
        Spacer(modifier = Modifier.height(50.dp))
        EmojiText(
          emojiText = "🥳",
          fontSize = 100.sp,
        )
        Spacer(modifier = Modifier.height(60.dp))
        FixedSizeText(
          text = "달성 완료 반다라트",
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
                    .height(52.dp)
                    .background(Gray100),
                  contentAlignment = Alignment.Center,
                ) {
                  EmojiText(
                    emojiText = "😎",
                    fontSize = 22.sp,
                  )
                }
              }
            }
            Spacer(modifier = Modifier.height(6.dp))
            FixedSizeText(
              text = "완벽한 2024년",
              color = Color.Black,
              fontWeight = FontWeight.W700,
              fontSize = 16.sp,
              letterSpacing = (-0.32).sp,
            )
            Spacer(modifier = Modifier.height(16.dp))
          }
        }
      }
      Box(
        modifier = Modifier
          .clickable(onClick = {})
          .align(Alignment.BottomCenter)
          .offset(y = (-110).dp),
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            val image = painterResource(id = R.drawable.ic_gallery)
            Image(
              painter = image,
              contentDescription = "Option Icon",
            )
            Text(
              text = buildAnnotatedString {
                withStyle(
                  style = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                  ),
                ) {
                  append("이미지 저장하기")
                }
              },
              color = Gray400,
              fontFamily = pretendard,
              fontWeight = FontWeight.W600,
              fontSize = 16.sp.nonScaleSp,
              letterSpacing = -(0.32).sp.nonScaleSp,
            )
          }
        }
      }
      BandalartButton(
        onClick = {},
        text = "링크 공유하기",
        modifier = Modifier
          .align(Alignment.BottomCenter)
          // .padding(bottom = 32.dp)
          .offset(y = (-32).dp),
      )
    }
  }
}
