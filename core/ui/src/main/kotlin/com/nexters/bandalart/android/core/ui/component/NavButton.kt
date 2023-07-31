package com.nexters.bandalart.android.core.ui.component

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nexters.bandalart.android.core.ui.theme.BandalartTheme

// TODO 사용하지 않으면 지우기
@Composable
fun NavButton(
  onClick: () -> Unit,
  route: String,
  modifier: Modifier = Modifier,
) {
  Button(
    onClick = onClick,
    modifier = modifier.wrapContentSize(),
  ) {
    Text(
      text = "Go to $route",
    )
  }
}

@Preview
@Composable
fun NavButtonPreview() {
  BandalartTheme {
    NavButton(onClick = {}, route = "Home")
  }
}
