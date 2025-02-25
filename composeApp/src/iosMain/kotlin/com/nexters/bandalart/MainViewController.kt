package com.nexters.bandalart

import androidx.compose.ui.window.ComposeUIViewController
import com.nexters.bandalart.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { BandalartApp() }
