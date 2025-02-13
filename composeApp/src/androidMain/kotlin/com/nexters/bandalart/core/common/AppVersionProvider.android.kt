package com.nexters.bandalart.core.common

import android.content.Context
import android.content.pm.PackageManager
import io.github.aakira.napier.Napier

actual class AppVersionProvider(private val context: Context) {
    actual fun getAppVersion(): String {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Napier.e("Failed to get package info", e, tag = "AppVersion")
            "Unknown"
        }
    }
}

