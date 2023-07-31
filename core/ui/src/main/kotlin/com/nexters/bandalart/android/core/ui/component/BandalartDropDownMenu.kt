package com.nexters.bandalart.android.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.R
import com.nexters.bandalart.android.core.ui.extension.nonScaleSp
import com.nexters.bandalart.android.core.ui.theme.Error
import com.nexters.bandalart.android.core.ui.theme.Gray800
import com.nexters.bandalart.android.core.ui.theme.White
import com.nexters.bandalart.android.core.ui.theme.pretendard

@Composable
fun BandalartDropDownMenu(
  modifier: Modifier = Modifier,
  onResult: (Boolean) -> Unit,
  isDropDownMenuExpanded: Boolean,
  onDeleteClicked: () -> Unit,
) {
  var dialogOpened by remember { mutableStateOf(false) }
  MaterialTheme(
    shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(12.dp)),
  ) {
    DropdownMenu(
      modifier = modifier
        .wrapContentSize()
        .background(White),
      expanded = isDropDownMenuExpanded,
      onDismissRequest = { onResult(false) },
      offset = DpOffset(
        x = (-50).dp,
        y = 0.dp,
      ),
    ) {
      DropdownMenuItem(
        modifier = Modifier
          .wrapContentSize()
          .padding(horizontal = 7.dp),
        text = {
          Row {
            val image = painterResource(id = R.drawable.ic_image)
            Image(
              painter = image,
              contentDescription = "Image Icon",
              modifier = Modifier.height(14.dp).align(CenterVertically),
            )
            Text(
              modifier = Modifier.fillMaxHeight().padding(start = 13.dp).align(CenterVertically),
              text = "이미지 내보내기",
              color = Gray800,
              fontSize = 14.sp.nonScaleSp,
              fontFamily = pretendard,
              fontWeight = FontWeight.W500,
            )
          }
        },
        onClick = { onResult(false) },
      )
      Spacer(modifier = Modifier.height(2.dp))
      DropdownMenuItem(
        modifier = Modifier
          .wrapContentSize()
          .padding(horizontal = 7.dp),
        text = {
          Row {
            val image = painterResource(id = R.drawable.ic_delete)
            Image(
              painter = image,
              contentDescription = "Delete Icon",
              modifier = Modifier.height(14.dp).align(CenterVertically),
              colorFilter = ColorFilter.tint(Error),
            )
            Text(
              modifier = Modifier.fillMaxHeight().padding(start = 13.dp).align(CenterVertically),
              text = "삭제하기",
              color = Error,
              fontSize = 14.sp.nonScaleSp,
              fontFamily = pretendard,
              fontWeight = FontWeight.W500,
            )
          }
        },
        onClick = { dialogOpened = true },
      )
      BandalartDeleteAlertDialog(
        title = "'8구단 드래프트 1순위'\n만다라트를 삭제하시겠어요?",
        message = "삭제된 만다라트는 다시 복구할 수 없어요.",
        dialogOpened = dialogOpened,
        onDeleteClicked = onDeleteClicked,
        onCancleClicked = { dialogOpened = !dialogOpened },
      )
    }
  }
}
