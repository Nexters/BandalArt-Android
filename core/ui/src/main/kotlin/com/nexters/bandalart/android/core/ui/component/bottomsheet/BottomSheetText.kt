package com.nexters.bandalart.android.core.ui.component.bottomsheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.component.FixedSizeText
import com.nexters.bandalart.android.core.designsystem.theme.Gray400
import com.nexters.bandalart.android.core.designsystem.theme.Gray600
import com.nexters.bandalart.android.core.designsystem.theme.Gray900
import com.nexters.bandalart.android.core.ui.R

@Composable
fun BottomSheetTitleText(
  isMainCell: Boolean,
  isSubCell: Boolean,
  isBlankCell: Boolean,
  modifier: Modifier = Modifier,
) {
  FixedSizeText(
    text =
    if (isBlankCell)
      if (isMainCell) stringResource(id = R.string.bottomsheet_header_maincell_enter_title)
      else if (isSubCell) stringResource(id = R.string.bottomsheet_header_subcell_enter_title)
      else stringResource(id = R.string.bottomsheet_header_taskcell_enter_title)
    else if (isMainCell) stringResource(id = R.string.bottomsheet_header_maincell_edit_title)
    else if (isSubCell) stringResource(id = R.string.bottomsheet_header_subcell_edit_title)
    else stringResource(id = R.string.bottomsheet_header_taskcell_edit_title),
    modifier = modifier.fillMaxWidth(),
    textAlign = TextAlign.Center,
    color = Gray900,
    fontSize = 16.sp,
    fontWeight = FontWeight.W700,
    letterSpacing = (-0.32).sp,
    lineHeight = 22.4.sp,
  )
}

@Composable
fun BottomSheetSubTitleText(
  modifier: Modifier = Modifier,
  text: String,
) {
  FixedSizeText(
    modifier = modifier,
    text = text,
    textAlign = TextAlign.Start,
    color = Gray600,
    fontSize = 12.sp,
    fontWeight = FontWeight.W700,
    letterSpacing = (-0.24).sp,
    lineHeight = 22.4.sp,
  )
}

@Composable
fun BottomSheetContentPlaceholder(
  modifier: Modifier = Modifier,
  text: String,
) {
  FixedSizeText(
    modifier = modifier,
    text = text,
    textAlign = TextAlign.Start,
    color = Gray400,
    fontSize = 16.sp,
    fontWeight = FontWeight.W400,
    letterSpacing = (-0.32).sp,
    lineHeight = 22.4.sp,
  )
}

@Composable
fun BottomSheetContentText(
  modifier: Modifier = Modifier,
  text: String,
) {
  FixedSizeText(
    modifier = modifier,
    text = text,
    textAlign = TextAlign.Start,
    color = Gray600,
    fontSize = 16.sp,
    fontWeight = FontWeight.W600,
    letterSpacing = (-0.32).sp,
    lineHeight = 22.4.sp,
  )
}

@Composable
fun BottomSheetButtonText(
  modifier: Modifier = Modifier,
  text: String,
  color: Color,
) {
  FixedSizeText(
    modifier = modifier,
    text = text,
    textAlign = TextAlign.Start,
    color = color,
    fontSize = 16.sp,
    fontWeight = FontWeight.W700,
    letterSpacing = (-0.32).sp,
    lineHeight = 21.sp,
  )
}
