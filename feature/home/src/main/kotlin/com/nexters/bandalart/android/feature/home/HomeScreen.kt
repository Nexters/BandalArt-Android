package com.nexters.bandalart.android.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.NavButton
import kotlinx.coroutines.launch

@Composable
internal fun HomeRoute(
  navigateToOnBoarding: () -> Unit,
  navigateToComplete: () -> Unit,
  onShowSnackbar: suspend (String) -> Boolean,
  modifier: Modifier = Modifier,
) {
  HomeScreen(
    navigateToOnBoarding = navigateToOnBoarding,
    navigateToComplete = navigateToComplete,
    onShowSnackbar = onShowSnackbar,
    modifier = modifier
  )
}

@Composable
internal fun HomeScreen(
  navigateToOnBoarding: () -> Unit,
  navigateToComplete: () -> Unit,
  onShowSnackbar: suspend (String) -> Boolean,
  modifier: Modifier = Modifier,
) {
  val scope = rememberCoroutineScope()

  Surface(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxSize()
    ) {
      Text(
        text = "Home Screen", textAlign = TextAlign.Center, fontSize = 16.sp
      )
      Spacer(modifier = Modifier.height(16.dp))
      Column(
        modifier = Modifier.wrapContentWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        NavButton(
          onClick = navigateToOnBoarding,
          route = "OnBoarding",
        )
        NavButton(
          onClick = navigateToComplete,
          route = "Complete",

          )
        Button(
          onClick = {
            scope.launch {
              onShowSnackbar("메세지 출력")
            }
          }
        ) {
          Text("스낵바 띄우기")
        }
      }
    }
  }
}