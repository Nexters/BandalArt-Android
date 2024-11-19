package com.nexters.bandalart.ui

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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.bandalart.core.designsystem.theme.Gray700
import com.nexters.bandalart.core.designsystem.theme.White
import com.nexters.bandalart.navigation.BandalartNavHost

@Composable
fun BandalartApp() {
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
                            Text(
                                text = it.visuals.message,
                                color = Gray700,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W600,
                                modifier = Modifier.align(Alignment.Center),
                                letterSpacing = -(0.24).sp,
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        BandalartNavHost(
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
