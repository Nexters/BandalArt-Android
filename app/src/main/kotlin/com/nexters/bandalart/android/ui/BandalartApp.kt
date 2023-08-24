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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.android.core.ui.component.FixedSizeText
import com.nexters.bandalart.android.core.ui.theme.Gray700
import com.nexters.bandalart.android.core.ui.theme.White
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
          .height(36.dp),
        hostState = snackbarHostState,
        snackbar = {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 40.dp),
            shape = RoundedCornerShape(50.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(8.dp),
          ) {
            Box(Modifier.fillMaxSize()) {
              FixedSizeText(
                text = it.visuals.message,
                fontWeight = FontWeight.W600,
                color = Gray700,
                fontSize = 12.sp,
                letterSpacing = -(0.24).sp,
                modifier = Modifier.align(Alignment.Center),
              )
            }
          }
        },
      )
    },
  ) { innerPadding ->
    BandalartNavHost(
      modifier = Modifier.padding(innerPadding),
      appState = appState,
      onShowSnackbar = { message ->
        snackbarHostState.showSnackbar(
          message = message,
          duration = SnackbarDuration.Short,
        ) == SnackbarResult.ActionPerformed
      },
    )
  }
}
