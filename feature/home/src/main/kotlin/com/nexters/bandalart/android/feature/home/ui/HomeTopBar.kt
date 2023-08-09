package com.nexters.bandalart.android.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.R
import com.nexters.bandalart.android.core.ui.component.FixedSizeText
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.theme.Gray600
import com.nexters.bandalart.android.core.ui.theme.White
import com.nexters.bandalart.android.core.ui.theme.pretendard

@Suppress("unused")
@Composable
internal fun HomeTopBar(
  bandalartCount: Int,
  onShowBandalartList: () -> Unit,
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(62.dp)
      .background(White),
    contentAlignment = Alignment.CenterStart,
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
    ) {
      val image = painterResource(id = R.drawable.ic_app)
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
          .padding(end = 20.dp)
          .clickable(onClick = onShowBandalartList),
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (bandalartCount > 1) {
            val image = painterResource(id = R.drawable.ic_hamburger)
            Image(
              painter = image,
              contentDescription = "Hamburger Icon",
            )
            Text(
              text = "목록",
              fontFamily = pretendard,
              fontWeight = FontWeight.W700,
              color = Gray600,
              fontSize = 16.sp.nonScaleSp,
            )
          } else {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "Add Icon",
              tint = Gray600,
              modifier = Modifier.size(20.dp),
            )
            FixedSizeText(
              text = "추가",
              color = Gray600,
              fontWeight = FontWeight.W700,
              fontSize = 16.sp,
            )
          }
        }
      }
    }
  }
}
