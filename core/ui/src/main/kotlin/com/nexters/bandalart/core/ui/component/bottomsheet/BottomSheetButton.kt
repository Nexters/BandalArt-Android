package com.nexters.bandalart.core.ui.component.bottomsheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray200
import com.nexters.bandalart.core.designsystem.theme.Gray400
import com.nexters.bandalart.core.designsystem.theme.Gray900
import com.nexters.bandalart.core.designsystem.theme.White
import com.nexters.bandalart.core.ui.ComponentPreview

@Composable
fun BottomSheetDeleteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        colors = IconButtonColors(Gray200, Gray900, Gray200, Gray900),
    ) {
        BottomSheetButtonText(
            text = stringResource(R.string.bottomsheet_delete),
            color = Gray900,
        )
    }
}

@Composable
fun BottomSheetCompleteButton(
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        colors = IconButtonColors(Gray900, White, Gray200, Gray400),
        enabled = isEnabled,
    ) {
        BottomSheetButtonText(
            text = stringResource(R.string.bottomsheet_done),
            color = if (isEnabled) White else Gray400,
        )
    }
}

@ComponentPreview
@Composable
private fun BottomSheetDeleteButtonPreview() {
    BandalartTheme {
        BottomSheetDeleteButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
    }
}

@ComponentPreview
@Composable
private fun BottomSheetCompleteButtonPreview() {
    BandalartTheme {
        BottomSheetCompleteButton(
            isEnabled = false,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
    }
}
