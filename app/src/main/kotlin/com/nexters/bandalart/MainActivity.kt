package com.nexters.bandalart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.core.designsystem.theme.Gray700
import com.nexters.bandalart.feature.splash.SplashScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var circuit: Circuit

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BandalartTheme {
                val backStack = rememberSaveableBackStack(root = SplashScreen)
                val navigator = rememberCircuitNavigator(backStack)

                val snackbarHostState = remember { SnackbarHostState() }
                val height = LocalConfiguration.current.screenHeightDp

                CircuitCompositionLocals(circuit) {
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
                        ContentWithOverlays {
                            NavigableCircuitContent(
                                navigator = navigator,
                                backStack = backStack,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                            )
                        }
                    }
                }
            }
        }
    }
}
