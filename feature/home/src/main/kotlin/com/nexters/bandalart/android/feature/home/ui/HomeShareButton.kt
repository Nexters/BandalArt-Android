package com.nexters.bandalart.android.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.R
import com.nexters.bandalart.android.core.designsystem.theme.Gray100
import com.nexters.bandalart.android.core.designsystem.theme.Gray900
import com.nexters.bandalart.android.core.ui.ComponentPreview
import com.nexters.bandalart.android.core.ui.component.FixedSizeText
import com.nexters.bandalart.android.core.ui.extension.clickableSingle
import com.nexters.bandalart.android.feature.home.model.BandalartDetailUiModel
import com.nexters.bandalart.android.feature.home.model.dummyBandalartDetailData

@Composable
fun HomeShareButton(
  bandalartDetailData: BandalartDetailUiModel,
  shareBandalart: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .wrapContentSize()
      .clip(RoundedCornerShape(18.dp))
      .background(Gray100)
      .clickableSingle { shareBandalart(bandalartDetailData.key) },
    contentAlignment = Alignment.Center,
  ) {
    Row(
      modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 20.dp, bottom = 8.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Image(
        imageVector = ImageVector.vectorResource(
          id = R.drawable.ic_share,
        ),
        contentDescription = stringResource(com.nexters.bandalart.android.core.ui.R.string.share_descrption),
      )
      FixedSizeText(
        text = stringResource(com.nexters.bandalart.android.core.ui.R.string.home_share),
        color = Gray900,
        fontSize = 12.sp,
        fontWeight = FontWeight.W700,
        modifier = Modifier.padding(start = 4.dp),
      )
    }
  }
}

@ComponentPreview
@Composable
fun HomeShareButtonPreview() {
  HomeShareButton(
    bandalartDetailData = dummyBandalartDetailData,
    shareBandalart = {},
  )
}
