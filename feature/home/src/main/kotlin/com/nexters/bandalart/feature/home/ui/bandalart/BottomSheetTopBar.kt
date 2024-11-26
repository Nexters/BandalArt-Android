@file:OptIn(ExperimentalMaterial3Api::class)

package com.nexters.bandalart.feature.home.ui.bandalart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray900
import com.nexters.bandalart.core.ui.ComponentPreview
import com.nexters.bandalart.core.ui.R
import com.nexters.bandalart.core.ui.component.bottomsheet.BottomSheetTitleText
import com.nexters.bandalart.feature.home.viewmodel.BottomSheetUiAction

@Composable
fun BottomSheetTopBar(
    isMainCell: Boolean,
    isSubCell: Boolean,
    isBlankCell: Boolean,
    onAction: (BottomSheetUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        BottomSheetTitleText(isMainCell = isMainCell, isSubCell = isSubCell, isBlankCell = isBlankCell)
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .height(21.dp)
                .aspectRatio(1f),
            onClick = {
                onAction(BottomSheetUiAction.OnClearButtonClick)
            },
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.clear_description),
                tint = Gray900,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun BottomSheetMainCellTopBarPreview() {
    BandalartTheme {
        BottomSheetTopBar(
            isMainCell = true,
            isSubCell = false,
            isBlankCell = false,
            onAction = {},
        )
    }
}

@ComponentPreview
@Composable
private fun BottomSheetSubCellTopBarPreview() {
    BandalartTheme {
        BottomSheetTopBar(
            isMainCell = false,
            isSubCell = true,
            isBlankCell = false,
            onAction = {},
        )
    }
}

@ComponentPreview
@Composable
private fun BottomSheetBlankCellTopBarPreview() {
    BandalartTheme {
        BottomSheetTopBar(
            isMainCell = false,
            isSubCell = false,
            isBlankCell = true,
            onAction = {},
        )
    }
}
