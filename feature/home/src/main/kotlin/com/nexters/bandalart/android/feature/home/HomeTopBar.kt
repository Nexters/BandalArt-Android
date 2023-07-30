package com.nexters.bandalart.android.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.android.core.ui.theme.Gray600
import com.nexters.bandalart.android.core.ui.theme.pretendard

@Suppress("unused")
@Composable
internal fun HomeTopBar(
  onAddBandalart: () -> Unit,
  onShowBandalartList: () -> Unit,
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(62.dp)
      .background(Color.White),
    contentAlignment = Alignment.CenterStart,
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
    ) {
      val image = painterResource(id = com.nexters.bandalart.android.core.designsystem.R.drawable.ic_app)
      Image(
        painter = image,
        contentDescription = "App Icon",
        modifier = Modifier
          .align(Alignment.CenterVertically)
          .padding(start = 20.dp),
      )
      Spacer(modifier = Modifier.weight(1f))
      Box(
        modifier = Modifier
          .align(Alignment.CenterVertically)
          .padding(end = 20.dp)
          // TODO ripple
          .clickable(onClick = onAddBandalart),
      ) {
        // TODO 반다라트 표가 여러개 일때는 아이콘, 텍스트 변경
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Icon",
            tint = Gray600,
          )
          Text(
            text = "추가",
            fontFamily = pretendard,
            fontWeight = FontWeight.W700,
            color = Gray600,
          )
        }
      }
    }
  }
}
