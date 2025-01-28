package com.nexters.bandalart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.feature.home.ShareScreen
import com.nexters.bandalart.feature.splash.SplashScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuitx.android.rememberAndroidScreenAwareNavigator

//@AndroidEntryPoint
//class MainActivity : ComponentActivity() {
//    @Inject
//    lateinit var circuit: Circuit
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        installSplashScreen()
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            BandalartTheme {
//                val backStack = rememberSaveableBackStack(root = SplashScreen)
//                val navigator = rememberAndroidScreenAwareNavigator(
//                    delegate = rememberCircuitNavigator(backStack),
//                    starter = ShareScreen.buildAndroidStarter(this),
//                )
//
//                CircuitCompositionLocals(circuit) {
//                    Scaffold { innerPadding ->
//                        ContentWithOverlays {
//                            NavigableCircuitContent(
//                                navigator = navigator,
//                                backStack = backStack,
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .padding(innerPadding),
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}

class MainActivity : ComponentActivity() {
    private lateinit var component: BandalartComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        component = (application as BandalartApplication).component

        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BandalartTheme {
                val backStack = rememberSaveableBackStack(root = SplashScreen)
                val navigator = rememberAndroidScreenAwareNavigator(
                    delegate = rememberCircuitNavigator(backStack),
                    starter = ShareScreen.buildAndroidStarter(this),
                )

                CircuitCompositionLocals(component.circuit()) {
                    Scaffold { innerPadding ->
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
