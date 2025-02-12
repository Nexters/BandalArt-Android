package com.nexters.bandalart.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
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
                    BandalartSnackbar(message = it.visuals.message)
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
