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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.android.core.designsystem.theme.Gray600
import com.nexters.bandalart.android.core.designsystem.theme.White
import com.nexters.bandalart.android.core.designsystem.theme.pretendard
import com.nexters.bandalart.android.core.ui.ComponentPreview
import com.nexters.bandalart.android.core.ui.R
import com.nexters.bandalart.android.core.ui.component.AppTitle

@Composable
internal fun HomeTopBar(
  bandalartCount: Int,
  onShowBandalartList: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(62.dp)
      .background(White),
    contentAlignment = Alignment.CenterStart,
  ) {
    Row(modifier = Modifier.fillMaxWidth()) {
      AppTitle(
        modifier = Modifier
          .align(Alignment.CenterVertically)
          .padding(start = 20.dp, top = 2.dp),
      )
      Spacer(modifier = Modifier.weight(1f))
      Box(
        modifier = Modifier
          .padding(end = 20.dp)
          .clickable(onClick = onShowBandalartList),
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (bandalartCount > 1) {
            Image(
              imageVector = ImageVector.vectorResource(
                id = com.nexters.bandalart.android.core.designsystem.R.drawable.ic_hamburger,
              ),
              contentDescription = stringResource(R.string.hamburger_descrption),
            )
            Text(
              text = stringResource(R.string.home_list),
              color = Gray600,
              fontSize = 16.sp,
              fontWeight = FontWeight.W700,
              fontFamily = pretendard,
            )
          } else {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = stringResource(R.string.add_descrption),
              tint = Gray600,
              modifier = Modifier.size(20.dp),
            )
            Text(
              text = stringResource(R.string.home_add),
              color = Gray600,
              fontSize = 16.sp,
              fontWeight = FontWeight.W700,
            )
          }
        }
      }
    }
  }
}

@ComponentPreview
@Composable
fun HomeTopBarSingleBandalartPreview() {
  BandalartTheme {
    HomeTopBar(
      bandalartCount = 1,
      onShowBandalartList = {},
    )
  }
}

@ComponentPreview
@Composable
fun HomeTopBarMultipleBandalartPreview() {
  BandalartTheme {
    HomeTopBar(
      bandalartCount = 2,
      onShowBandalartList = {},
    )
  }
}
