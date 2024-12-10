package com.nexters.bandalart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.ui.BandalartApp
import dagger.hilt.android.AndroidEntryPoint

// TODO 선택 업데이트가 발생하여야 하는데, 강제 업데이트가 트리거 됨
// TODO 강제 업데이트 진행시 앱이 종료되는 문제(한 3번째 다시 시도하면 정상 업데이트 진행됨)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            BandalartTheme {
                BandalartApp()
            }
        }
    }
}
