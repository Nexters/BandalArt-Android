package com.nexters.bandalart.android.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.android.navigation.BandalartNavHost

@Composable
fun BandalartApp(
  appState: BandalartAppState = rememberBandalartAppState(),
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val height = LocalConfiguration.current.screenHeightDp

  Scaffold(
    snackbarHost = {
      SnackbarHost(
        modifier = Modifier
          .padding(bottom = (height - 96).dp)
          .height(56.dp),
        hostState = snackbarHostState,
        snackbar = {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
              containerColor = Color.White,
            ),
            elevation = CardDefaults.cardElevation(8.dp),
          ) {
            Box(
              Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            ) {
              Text(
                modifier = Modifier.align(Alignment.Center),
                text = it.visuals.message,
                color = Color.Black,
              )
            }
          }
        },
      )
    },
  ) { innerPadding ->
    BandalartNavHost(
      appState = appState,
      modifier = Modifier.padding(innerPadding),
      onShowSnackbar = { message ->
        snackbarHostState.showSnackbar(
          message = message,
          duration = SnackbarDuration.Short,
        ) == SnackbarResult.ActionPerformed
      },
    )
  }
}
