package com.nexters.bandalart.core.common

import android.app.Application
import android.content.pm.PackageManager
import io.github.aakira.napier.Napier
import org.koin.dsl.module
import org.koin.core.component.get

private val androidModule = module {
    single { get<Application>().packageManager }  // 이미 Koin에 Application이 주입되어 있다고 가정
}

actual fun getAppVersion(): String {
    val context: Application = get()  // Koin으로부터 Application 컨텍스트 획득

    return try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        Napier.e("Failed to get package info", e, tag = "AppVersion")
        "Unknown"
    }
}
