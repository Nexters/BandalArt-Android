package com.nexters.bandalart.feature.home

import android.content.Context
import android.net.Uri
import androidx.annotation.UiContext
import com.nexters.bandalart.core.common.extension.shareImage
import com.slack.circuitx.android.AndroidScreen
import com.slack.circuitx.android.AndroidScreenStarter
import kotlinx.parcelize.Parcelize

@Parcelize
@JvmInline
value class ShareScreen(val imageUri: String) : AndroidScreen {
    companion object {
        fun buildAndroidStarter(@UiContext context: Context): AndroidScreenStarter =
            AndroidScreenStarter { screen ->
                if (screen is ShareScreen) {
                    context.shareImage(Uri.parse(screen.imageUri))
                    true
                } else {
                    false
                }
            }
    }
}
