package com.nexters.bandalart.android.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.component.BandalartDropDownMenu
import com.nexters.bandalart.android.core.ui.component.EmojiText
import com.nexters.bandalart.android.core.ui.component.FixedSizeText
import com.nexters.bandalart.android.core.ui.extension.toColor
import com.nexters.bandalart.android.core.ui.extension.toFormatDate
import com.nexters.bandalart.android.core.designsystem.theme.Gray100
import com.nexters.bandalart.android.core.designsystem.theme.Gray300
import com.nexters.bandalart.android.core.designsystem.theme.Gray600
import com.nexters.bandalart.android.core.designsystem.theme.Gray900
import com.nexters.bandalart.android.core.designsystem.theme.MainColor
import com.nexters.bandalart.android.feature.home.HomeUiState

@Composable
fun HomeHeader(
  modifier: Modifier = Modifier,
  uiState: HomeUiState,
  openDropDownMenu: (Boolean) -> Unit,
  openEmojiBottomSheet: (Boolean) -> Unit,
  openBandalartDeleteAlertDialog: (Boolean) -> Unit,
  openCellBottomSheet: (Boolean) -> Unit,
) {
  Column(
    modifier.padding(horizontal = 16.dp),
  ) {
    Spacer(modifier = Modifier.height(24.dp))
    Column {
      Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
        Card(
          shape = RoundedCornerShape(16.dp),
          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
          Box(
            modifier = Modifier
              .width(52.dp)
              .aspectRatio(1f)
              .background(Gray100)
              .clickable { openEmojiBottomSheet(true) },
            contentAlignment = Alignment.Center,
          ) {
            if (uiState.bandalartDetailData?.profileEmoji.isNullOrEmpty()) {
              Image(
                painter = painterResource(com.nexters.bandalart.android.core.designsystem.R.drawable.ic_empty_emoji),
                contentDescription = stringResource(com.nexters.bandalart.android.core.ui.R.string.empty_emoji_descrption),
              )
            } else {
              EmojiText(
                emojiText = uiState.bandalartDetailData?.profileEmoji,
                fontSize = 22.sp,
              )
            }
          }
        }
        if (uiState.bandalartDetailData?.profileEmoji.isNullOrEmpty()) {
          Image(
            painter = painterResource(com.nexters.bandalart.android.core.designsystem.R.drawable.ic_edit),
            contentDescription = stringResource(com.nexters.bandalart.android.core.ui.R.string.edit_descrption),
            modifier = Modifier
              .align(Alignment.BottomEnd)
              .offset(x = 4.dp, y = 4.dp),
          )
        }
      }
      Spacer(modifier = Modifier.height(12.dp))
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight(),
      ) {
        FixedSizeText(
          text = uiState.bandalartDetailData?.title
            ?: stringResource(com.nexters.bandalart.android.core.ui.R.string.home_empty_title),
          color = if (uiState.bandalartDetailData?.title.isNullOrEmpty()) Gray300 else Gray900,
          fontWeight = FontWeight.W700,
          fontSize = 20.sp,
          letterSpacing = (-0.4).sp,
          modifier = Modifier
            .align(Alignment.Center)
            .clickable { openCellBottomSheet(true) },
        )
        Image(
          painter = painterResource(com.nexters.bandalart.android.core.designsystem.R.drawable.ic_option),
          contentDescription = stringResource(com.nexters.bandalart.android.core.ui.R.string.option_descrption),
          modifier = Modifier
            .align(Alignment.CenterEnd)
            .clickable(onClick = { openDropDownMenu(true) }),
        )
        BandalartDropDownMenu(
          openDropDownMenu = openDropDownMenu,
          isDropDownMenuOpened = uiState.isDropDownMenuOpened,
          onDeleteClicked = {
            openBandalartDeleteAlertDialog(true)
            openDropDownMenu(false)
          },
        )
      }
    }
    Spacer(modifier = Modifier.height(24.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      FixedSizeText(
        text = stringResource(
          com.nexters.bandalart.android.core.ui.R.string.home_complete_ratio,
          uiState.bandalartDetailData?.completionRatio ?: 0,
        ),
        color = Gray600,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp,
        letterSpacing = (-0.24).sp,
      )
      if (!uiState.bandalartDetailData?.dueDate.isNullOrEmpty()) {
        VerticalDivider(
          modifier = Modifier
            .height(8.dp)
            .padding(start = 6.dp),
          thickness = 1.dp,
          color = Gray300,
        )
        FixedSizeText(
          text = uiState.bandalartDetailData?.dueDate!!.toFormatDate(),
          color = Gray600,
          fontWeight = FontWeight.W500,
          fontSize = 12.sp,
          letterSpacing = (-0.24).sp,
          modifier = Modifier.padding(start = 6.dp),
        )
      }
      Spacer(modifier = Modifier.weight(1f))
      if (uiState.bandalartDetailData != null && uiState.bandalartDetailData.isCompleted) {
        Box(
          modifier
            .clip(RoundedCornerShape(24.dp))
            .background(color = uiState.bandalartDetailData.mainColor.toColor()),
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = stringResource(com.nexters.bandalart.android.core.ui.R.string.check_descrption),
              tint = Gray900,
              modifier = Modifier.size(13.dp),
            )
            FixedSizeText(
              text = stringResource(com.nexters.bandalart.android.core.ui.R.string.home_complete),
              color = Gray900,
              fontWeight = FontWeight.W600,
              fontSize = 10.sp,
              letterSpacing = (-0.2).sp,
              modifier = Modifier.padding(start = 2.dp),
            )
          }
        }
      }
    }
    Spacer(modifier = Modifier.height(8.dp))
    CompletionRatioProgressBar(
      completionRatio = uiState.bandalartCellData?.completionRatio ?: 0,
      progressColor = (uiState.bandalartCellData?.mainColor?.toColor() ?: MainColor),
    )
    Spacer(modifier = Modifier.height(18.dp))
  }
}
