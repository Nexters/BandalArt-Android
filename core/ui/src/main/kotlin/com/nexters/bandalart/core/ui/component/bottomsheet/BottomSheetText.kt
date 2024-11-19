package com.nexters.bandalart.core.ui.component.bottomsheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray400
import com.nexters.bandalart.core.designsystem.theme.Gray600
import com.nexters.bandalart.core.designsystem.theme.Gray900
import com.nexters.bandalart.core.ui.ComponentPreview
import com.nexters.bandalart.core.ui.R

@Composable
fun BottomSheetTitleText(
    isMainCell: Boolean,
    isSubCell: Boolean,
    isBlankCell: Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        text =
        if (isBlankCell)
            if (isMainCell) stringResource(id = R.string.bottomsheet_header_maincell_enter_title)
            else if (isSubCell) stringResource(id = R.string.bottomsheet_header_subcell_enter_title)
            else stringResource(id = R.string.bottomsheet_header_taskcell_enter_title)
        else if (isMainCell) stringResource(id = R.string.bottomsheet_header_maincell_edit_title)
        else if (isSubCell) stringResource(id = R.string.bottomsheet_header_subcell_edit_title)
        else stringResource(id = R.string.bottomsheet_header_taskcell_edit_title),
        color = Gray900,
        fontSize = 16.sp,
        fontWeight = FontWeight.W700,
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        letterSpacing = (-0.32).sp,
        lineHeight = 22.4.sp,
    )
}

@Composable
fun BottomSheetSubTitleText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = Gray600,
        fontSize = 12.sp,
        fontWeight = FontWeight.W700,
        modifier = modifier,
        textAlign = TextAlign.Start,
        letterSpacing = (-0.24).sp,
        lineHeight = 22.4.sp,
    )
}

@Composable
fun BottomSheetContentPlaceholder(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = Gray400,
        fontSize = 16.sp,
        fontWeight = FontWeight.W400,
        modifier = modifier,
        textAlign = TextAlign.Start,
        letterSpacing = (-0.32).sp,
        lineHeight = 22.4.sp,
    )
}

@Composable
fun BottomSheetContentText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        textAlign = TextAlign.Start,
        color = Gray600,
        fontSize = 16.sp,
        fontWeight = FontWeight.W600,
        modifier = modifier,
        letterSpacing = (-0.32).sp,
        lineHeight = 22.4.sp,
    )
}

@Composable
fun BottomSheetButtonText(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        textAlign = TextAlign.Start,
        color = color,
        fontSize = 16.sp,
        fontWeight = FontWeight.W700,
        modifier = modifier,
        letterSpacing = (-0.32).sp,
        lineHeight = 21.sp,
    )
}

@ComponentPreview
@Composable
private fun BottomSheetMainCellTitleTextPreview() {
    BandalartTheme {
        BottomSheetTitleText(
            isMainCell = true,
            isSubCell = false,
            isBlankCell = false,
        )
    }
}

@ComponentPreview
@Composable
private fun BottomSheetSubCellTitleTextPreview() {
    BandalartTheme {
        BottomSheetTitleText(
            isMainCell = false,
            isSubCell = true,
            isBlankCell = false,
        )
    }
}

@ComponentPreview
@Composable
private fun BottomSheetBlankCellTitleTextPreview() {
    BandalartTheme {
        BottomSheetTitleText(
            isMainCell = false,
            isSubCell = false,
            isBlankCell = true,
        )
    }
}

@ComponentPreview
@Composable
private fun BottomSheetSubTitleTextPreview() {
    BandalartTheme {
        BottomSheetSubTitleText(text = "목표 이름 (필수)")
    }
}

@ComponentPreview
@Composable
private fun BottomSheetContentPlaceholderPreview() {
    BandalartTheme {
        BottomSheetContentPlaceholder(text = "15자 이내로 입력해주세요")
    }
}

@ComponentPreview
@Composable
private fun BottomSheetContentTextPreview() {
    BandalartTheme {
        BottomSheetContentText(text = "달성")
    }
}

@ComponentPreview
@Composable
private fun BottomSheetButtonTextPreview() {
    BandalartTheme {
        BottomSheetButtonText(
            text = "완료",
            color = Gray400,
        )
    }
}
