package com.nexters.bandalart.core.common.utils

import com.nexters.bandalart.core.common.BuildConfig

fun isValidImmediateAppUpdate(updateVersion: Int): Boolean {
    val updateMajor = updateVersion / 10000
    val updateMinor = (updateVersion % 10000) / 100

    val currentMajor = BuildConfig.VERSION_CODE / 10000
    val currentMinor = (BuildConfig.VERSION_CODE % 10000) / 100

    return updateMajor > currentMajor || updateMinor > currentMinor
}
