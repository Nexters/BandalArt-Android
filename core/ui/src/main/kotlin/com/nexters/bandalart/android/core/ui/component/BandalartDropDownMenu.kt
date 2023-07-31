package com.nexters.bandalart.android.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.theme.Gray800
import com.nexters.bandalart.android.core.ui.theme.Red
import com.nexters.bandalart.android.core.ui.theme.White
import com.nexters.bandalart.android.core.ui.theme.pretendard

@Composable
fun BandalartDropDownMenu(
  modifier: Modifier = Modifier,
  onResult: (Boolean) -> Unit,
  isDropDownMenuExpanded: Boolean,
  onDeleteClicked: () -> Unit,
) {
  MaterialTheme(
    shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(12.dp))
  ) {
    DropdownMenu(
      modifier = Modifier
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
          .padding(horizontal = 12.dp),
        text = {
          Text(
            text = "이미지 내보내기",
            color = Gray800,
            fontSize = 14.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.W500,
          )
        },
        onClick = { onResult(false) },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Image,
            contentDescription = "Image Icon",
          )
        },
      )
      Spacer(modifier = Modifier.height(2.dp))
      DropdownMenuItem(
        modifier = Modifier
          .wrapContentSize()
          .padding(horizontal = 12.dp),
        text = {
          Text(
            text = "삭제하기",
            color = Red,
            fontSize = 14.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.W500,
          )
        },
        onClick = { },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Icon",
            tint = Red,
          )
        },
      )
    }
  }
}
