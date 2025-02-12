package com.nexters.bandalart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nexters.bandalart.core.designsystem.theme.BandalartTheme
import com.nexters.bandalart.ui.BandalartApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        val dao = getDatabaseBuilder(applicationContext).peopleDao()
        setContent {
            BandalartTheme {
                BandalartApp()
            }
//            App(
//                dao,
//                prefs = remember {
//                    createDataStore(applicationContext)
//                }
//            )
        }
    }
}
