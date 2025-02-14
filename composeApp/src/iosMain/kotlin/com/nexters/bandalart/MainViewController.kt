package com.nexters.bandalart

import androidx.compose.ui.window.ComposeUIViewController
import com.nexters.bandalart.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
        // TODO info.plist 파일을 생성해서 적용하는 방법으로 대체
        enforceStrictPlistSanityCheck = false
    }
) { BandalartApp() }

