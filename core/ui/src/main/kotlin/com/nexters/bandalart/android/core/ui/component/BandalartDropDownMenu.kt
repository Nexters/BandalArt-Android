package com.nexters.bandalart.android.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.designsystem.theme.Error
import com.nexters.bandalart.android.core.designsystem.theme.White
import com.nexters.bandalart.android.core.designsystem.theme.pretendard
import com.nexters.bandalart.android.core.ui.ComponentPreview
import com.nexters.bandalart.android.core.ui.R

@Composable
fun BandalartDropDownMenu(
  openDropDownMenu: (Boolean) -> Unit,
  isDropDownMenuOpened: Boolean,
  onDeleteClicked: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  MaterialTheme(
    shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(12.dp)),
  ) {
    DropdownMenu(
      modifier = modifier
        .wrapContentSize()
        .background(White),
      expanded = isDropDownMenuOpened,
      onDismissRequest = { openDropDownMenu(false) },
      offset = DpOffset(
        x = (-18).dp,
        y = 0.dp,
      ),
    ) {
//      // MVP 에서 제외
//      DropdownMenuItem(
//        modifier = Modifier
//          .wrapContentSize()
//          .padding(horizontal = 7.dp),
//        text = {
//          Row {
//            Image(
//              imageVector = ImageVector.vectorResource(id = R.drawable.ic_image),
//              contentDescription = "Image Icon",
//              modifier = Modifier
//                .height(14.dp)
//                .align(CenterVertically),
//            )
//            Text(
//              modifier = Modifier
//                .fillMaxHeight()
//                .padding(start = 13.dp)
//                .align(CenterVertically),
//              text = "이미지 내보내기",
//              color = Gray800,
//              fontSize = 14.sp.nonScaleSp,
//              fontFamily = pretendard,
//              fontWeight = FontWeight.W500,
//            )
//          }
//        },
//        onClick = { openDropDownMenu(false)}
//      )
//      Spacer(modifier = Modifier.height(2.dp))
      DropdownMenuItem(
        modifier = Modifier
          .wrapContentSize()
          .padding(horizontal = 7.dp),
        text = {
          Row {
            Image(
              imageVector = ImageVector.vectorResource(
                id = com.nexters.bandalart.android.core.designsystem.R.drawable.ic_delete,
              ),
              contentDescription = context.getString(R.string.delete_descrption),
              modifier = Modifier
                .height(14.dp)
                .align(CenterVertically),
              colorFilter = ColorFilter.tint(Error),
            )
            FixedSizeText(
              modifier = Modifier
                .fillMaxHeight()
                .padding(start = 13.dp)
                .align(CenterVertically),
              text = context.getString(R.string.dropdown_delete),
              color = Error,
              fontSize = 14.sp,
              fontFamily = pretendard,
              fontWeight = FontWeight.W500,
            )
          }
        },
        onClick = onDeleteClicked,
      )
    }
  }
}

@ComponentPreview
@Composable
fun BandalartDropDownMenuPreview() {
  BandalartDropDownMenu(
    openDropDownMenu = {},
    isDropDownMenuOpened = true,
    onDeleteClicked = {}
  )
}
