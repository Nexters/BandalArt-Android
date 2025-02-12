package com.nexters.bandalart.feature.home.ui.bandalart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bandalart.composeapp.generated.resources.Res
import com.nexters.bandalart.core.designsystem.theme.Error
import com.nexters.bandalart.core.designsystem.theme.Gray800
import com.nexters.bandalart.core.designsystem.theme.pretendard
import com.nexters.bandalart.feature.home.viewmodel.HomeUiAction
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun BandalartDropDownMenu(
    isDropDownMenuOpened: Boolean,
    onAction: (HomeUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        modifier = modifier
            .wrapContentSize()
            .background(White),
        expanded = isDropDownMenuOpened,
        onDismissRequest = {
            onAction(HomeUiAction.OnDropDownMenuDismiss)
        },
        offset = DpOffset(
            x = (-18).dp,
            y = 0.dp,
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        DropdownMenuItem(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 7.dp),
            text = {
                Row {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_gallery),
                        contentDescription = "Gallery Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .align(CenterVertically),
                        tint = Color.Unspecified,
                    )
                    Text(
                        text = stringResource(Res.string.dropdown_save),
                        color = Gray800,
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
            onClick = {
                onAction(HomeUiAction.OnSaveClick)
            },
        )
        Spacer(modifier = Modifier.height(2.dp))
        DropdownMenuItem(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 7.dp),
            text = {
                Row {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_trash),
                        contentDescription = stringResource(Res.string.delete_description),
                        modifier = Modifier
                            .size(24.dp)
                            .align(CenterVertically),
                        tint = Color.Unspecified,
                    )
                    Text(
                        text = stringResource(Res.string.dropdown_delete),
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
            onClick = {
                onAction(HomeUiAction.OnDeleteClick)
            },
        )
    }
}

//@ComponentPreview
//@Composable
//private fun BandalartDropDownMenuPreview() {
//    BandalartTheme {
//        BandalartDropDownMenu(
//            isDropDownMenuOpened = true,
//            onAction = {},
//        )
//    }
//}
