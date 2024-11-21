package com.nexters.bandalart.core.common.extension

import android.content.Context
import android.graphics.Bitmap.CompressFormat.PNG
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

fun Context.getCurrentLocale(): Locale {
    return this.resources.configuration.locales.get(0)
}

@Suppress("TooGenericExceptionCaught")
fun Context.externalShareForBitmap(bitmap: ImageBitmap) {
    try {
        val file = File(bitmap.saveToDisk(this))
        val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)

        ShareCompat.IntentBuilder(this)
            .setStream(uri)
            .setType("image/png")
            .startChooser()
    } catch (e: Exception) {
        Timber.e("[externalShareFoBitmap] message: ${e.message}")
    }
}

@Suppress("TooGenericExceptionCaught")
fun Context.bitmapToFileUri(bitmap: ImageBitmap): Uri? {
    return try {
        val file = File(bitmap.saveToDisk(this))
        FileProvider.getUriForFile(this, "$packageName.provider", file)
    } catch (e: Exception) {
        Timber.e("Failed to convert bitmap to URI: ${e.message}")
        null
    }
}

@Suppress("TooGenericExceptionCaught")
fun Context.shareImage(imageUri: Uri) {
    try {
        ShareCompat.IntentBuilder(this)
            .setStream(imageUri)
            .setType("image/png")
            .startChooser()
    } catch (e: Exception) {
        Timber.e("Failed to share image: ${e.message}")
    }
}

private fun ImageBitmap.saveToDisk(context: Context): String {
    val fileName = "shared_image_${System.currentTimeMillis()}.png"
    val cachePath = File(context.cacheDir, "images").also { it.mkdirs() }
    val file = File(cachePath, fileName)
    val outputStream = FileOutputStream(file)

    asAndroidBitmap().compress(PNG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    return file.absolutePath
}
