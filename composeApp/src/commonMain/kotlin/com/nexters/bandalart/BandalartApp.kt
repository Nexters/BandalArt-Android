package com.nexters.bandalart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.navigation.BandalartNavHost
import com.nexters.bandalart.ui.BandalartSnackbar
import org.koin.compose.KoinContext

@Composable
fun BandalartApp() {
    BandalartTheme {
        KoinContext {
            val snackbarHostState = remember { SnackbarHostState() }

            Scaffold(
                snackbarHost = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter,
                    ) {
                        SnackbarHost(
                            modifier = Modifier
                                .padding(top = 96.dp)
                                .height(36.dp),
                            hostState = snackbarHostState,
                            snackbar = {
                                BandalartSnackbar(message = it.visuals.message)
                            },
                        )
                    }
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
    }
}
