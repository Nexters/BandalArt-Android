package com.nexters.bandalart.feature.home.ui.bandalart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.core.common.extension.clearFocusOnKeyboardDismiss
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.BottomSheetContent
import com.nexters.bandalart.core.ui.ComponentPreview

@Composable
internal fun BandalartTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    focusManager: FocusManager = LocalFocusManager.current,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(24.dp)
            .clearFocusOnKeyboardDismiss(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        maxLines = 1,
        textStyle = BottomSheetContent,
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                BottomSheetContentPlaceholder(text = placeholder)
            }
            innerTextField()
        },
    )
}

@ComponentPreview
@Composable
private fun BandalartTextFieldPreview() {
    BandalartTheme {
        BandalartTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Placeholder",
        )
    }
}
