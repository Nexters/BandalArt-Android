package com.nexters.bandalart.core.ui.component

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Error
import com.nexters.bandalart.core.designsystem.theme.White
import com.nexters.bandalart.core.designsystem.theme.pretendard
import com.nexters.bandalart.core.ui.ComponentPreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.designsystem.R as DesignR

@Composable
fun BandalartDropDownMenu(
    isDropDownMenuOpened: Boolean,
    toggleDropDownMenu: (Boolean) -> Unit,
    onDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MaterialTheme(
        shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(12.dp)),
    ) {
        DropdownMenu(
            modifier = modifier
                .wrapContentSize()
                .background(White),
            expanded = isDropDownMenuOpened,
            onDismissRequest = { toggleDropDownMenu(false) },
            offset = DpOffset(
                x = (-18).dp,
                y = 0.dp,
            ),
        ) {
//            // TODO MVP 에서 제외, 이번에 구현해도 좋을듯
//            DropdownMenuItem(
//                modifier = Modifier
//                    .wrapContentSize()
//                    .padding(horizontal = 7.dp),
//                text = {
//                    Row {
//                        Image(
//                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_image),
//                            contentDescription = "Image Icon",
//                            modifier = Modifier
//                                .height(14.dp)
//                                .align(CenterVertically),
//                        )
//                        Text(
//                            text = "이미지 내보내기",
//                            color = Gray800,
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.W500,
//                            modifier = Modifier
//                                .fillMaxHeight()
//                                .padding(start = 13.dp)
//                                .align(CenterVertically),
//                            fontFamily = pretendard,
//                        )
//                    }
//                },
//                onClick = { openDropDownMenu(false) },
//            )
//            Spacer(modifier = Modifier.height(2.dp))
            DropdownMenuItem(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 7.dp),
                text = {
                    Row {
                        Image(
                            imageVector = ImageVector.vectorResource(
                                id = DesignR.drawable.ic_delete,
                            ),
                            contentDescription = stringResource(R.string.delete_description),
                            modifier = Modifier
                                .height(14.dp)
                                .align(CenterVertically),
                            colorFilter = ColorFilter.tint(Error),
                        )
                        Text(
                            text = stringResource(R.string.dropdown_delete),
                            color = Error,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = 13.dp)
                                .align(CenterVertically),
                            fontFamily = pretendard,
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
private fun BandalartDropDownMenuPreview() {
    BandalartTheme {
        BandalartDropDownMenu(
            toggleDropDownMenu = {},
            isDropDownMenuOpened = true,
            onDeleteClicked = {},
        )
    }
}
