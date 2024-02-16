package com.nexters.bandalart.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.nexters.bandalart.android.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.android.ui.BandalartApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    setContent {
      BandalartTheme {
        BandalartApp()
      }
    }
  }
}
