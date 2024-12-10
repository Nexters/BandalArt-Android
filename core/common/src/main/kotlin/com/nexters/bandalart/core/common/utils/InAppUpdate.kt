package com.nexters.bandalart.core.common.utils

fun isValidImmediateAppUpdate(
    updateVersionCode: String,
    currentVersionCode: String,
): Boolean {
    // Major 버전 비교 (앞 2자리)
    val updateMajor = updateVersionCode.take(2).toInt()
    val currentMajor = currentVersionCode.take(2).toInt()

    // Minor 버전 비교 (중간 2자리)
    val updateMinor = updateVersionCode.substring(2, 4).toInt()
    val currentMinor = currentVersionCode.substring(2, 4).toInt()

    return updateMajor > currentMajor || updateMinor > currentMinor
}
