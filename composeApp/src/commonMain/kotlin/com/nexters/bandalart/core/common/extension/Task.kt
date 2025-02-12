package com.nexters.bandalart.core.common.extension

import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun Task<AppUpdateInfo>.await(): AppUpdateInfo {
    return suspendCoroutine { continuation ->
        addOnCompleteListener { result ->
            if (result.isSuccessful) {
                continuation.resume(result.result)
            } else {
                result.exception?.let { continuation.resumeWithException(it) }
            }
        }
    }
}
