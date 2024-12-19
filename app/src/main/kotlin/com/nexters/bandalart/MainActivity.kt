package com.nexters.bandalart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
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

                CircuitCompositionLocals(circuit) {
                    ContentWithOverlays {
                        NavigableCircuitContent(
                            navigator = navigator,
                            backStack = backStack,
//                            decoration = GestureNavigationDecoration(
//                                circuit.defaultNavDecoration,
//                                navigator::pop,
//                            )
                        )
                    }
                }

                // BandalartApp()
            }
        }
    }
}
