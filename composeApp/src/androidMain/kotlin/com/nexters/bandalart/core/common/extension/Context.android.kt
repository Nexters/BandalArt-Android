// androidMain
package com.nexters.bandalart.core.common.extension

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap.CompressFormat.PNG
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import com.eygraber.uri.Uri as KmpUri
import android.net.Uri as AndroidUri
import io.github.aakira.napier.Napier
import kotlinx.datetime.Clock
import java.io.File
import java.io.FileOutputStream

//actual class PlatformContext(val context: Context) {
//    val packageName: String get() = context.packageName
//    val cacheDir: File get() = context.cacheDir
//    val contentResolver: ContentResolver get() = context.contentResolver
//}
//
//private fun AndroidUri.toKmpUri(): KmpUri = KmpUri.parse(toString())
//private fun KmpUri.toAndroidUri(): AndroidUri = AndroidUri.parse(toString())
//
//@Suppress("TooGenericExceptionCaught")
//actual fun PlatformContext.externalShareForBitmap(bitmap: ImageBitmap) {
//    try {
//        val file = File(bitmap.saveToDisk(this))
//        val androidUri = FileProvider.getUriForFile(context, "$packageName.provider", file)
//
//        ShareCompat.IntentBuilder(context)
//            .setStream(androidUri)
//            .setType("image/png")
//            .startChooser()
//    } catch (e: Exception) {
//        Napier.e("[externalShareFoBitmap] message: ${e.message}")
//    }
//}
//
//@Suppress("TooGenericExceptionCaught")
//actual fun PlatformContext.bitmapToFileUri(bitmap: ImageBitmap): KmpUri? {
//    return try {
//        val file = File(bitmap.saveToDisk(this))
//        FileProvider.getUriForFile(context, "$packageName.provider", file).toKmpUri()
//    } catch (e: Exception) {
//        Napier.e("Failed to convert bitmap to URI: ${e.message}")
//        null
//    }
//}
//
//@Suppress("TooGenericExceptionCaught")
//actual fun PlatformContext.shareImage(imageUri: KmpUri) {
//    try {
//        ShareCompat.IntentBuilder(context)
//            .setStream(imageUri.toAndroidUri())
//            .setType("image/png")
//            .startChooser()
//    } catch (e: Exception) {
//        Napier.e("Failed to share image: ${e.message}")
//    }
//}
//
//actual fun ImageBitmap.saveToDisk(context: PlatformContext): String {
//    val fileName = "shared_image_${Clock.System.now().toEpochMilliseconds()}.png"
//    val cachePath = File(context.cacheDir, "images").also { it.mkdirs() }
//    val file = File(cachePath, fileName)
//    val outputStream = FileOutputStream(file)
//
//    asAndroidBitmap().compress(PNG, 100, outputStream)
//    outputStream.flush()
//    outputStream.close()
//
//    return file.absolutePath
//}
//
//@Suppress("TooGenericExceptionCaught")
//actual fun PlatformContext.saveImageToGallery(bitmap: ImageBitmap) {
//    try {
//        val fileName = "bandalart_${Clock.System.now().toEpochMilliseconds()}.png"
//        val contentValues = ContentValues().apply {
//            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
//            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
//                put(MediaStore.Images.Media.IS_PENDING, 1)
//            }
//        }
//
//        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//        imageUri?.let { uri ->
//            contentResolver.openOutputStream(uri)?.use { outputStream ->
//                bitmap.asAndroidBitmap().compress(PNG, 100, outputStream)
//            }
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                contentValues.clear()
//                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
//                contentResolver.update(uri, contentValues, null, null)
//            }
//        }
//    } catch (e: Exception) {
//        Napier.e("Failed to save image to gallery: ${e.message}")
//    }
//}
//
//@Suppress("TooGenericExceptionCaught")
//actual fun PlatformContext.saveUriToGallery(imageUri: KmpUri) {
//    try {
//        val fileName = "bandalart_${Clock.System.now().toEpochMilliseconds()}.png"
//        val contentValues = createContentValues(fileName)
//        val destinationUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//            ?: return
//
//        copyUriContent(imageUri.toAndroidUri(), destinationUri)
//        updatePendingStatus(destinationUri, contentValues)
//    } catch (e: Exception) {
//        Napier.e("Failed to save image to gallery: ${e.message}")
//    }
//}
//
//private fun createContentValues(fileName: String) = ContentValues().apply {
//    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
//    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
//        put(MediaStore.Images.Media.IS_PENDING, 1)
//    }
//}
//
//private fun PlatformContext.copyUriContent(sourceUri: AndroidUri, destinationUri: AndroidUri) {
//    contentResolver.openInputStream(sourceUri)?.use { input ->
//        contentResolver.openOutputStream(destinationUri)?.use { output ->
//            input.copyTo(output)
//        }
//    }
//}
//
//private fun PlatformContext.updatePendingStatus(uri: AndroidUri, contentValues: ContentValues) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//        contentValues.clear()
//        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
//        contentResolver.update(uri, contentValues, null, null)
//    }
//}
