package com.nexters.bandalart.core.common.extension

import androidx.compose.ui.graphics.ImageBitmap
import com.eygraber.uri.Uri
import platform.UIKit.*
import platform.Foundation.*
import io.github.aakira.napier.Napier
import platform.Photos.PHAssetChangeRequest
import platform.Photos.PHPhotoLibrary

actual typealias PlatformContext = UIViewController

@Suppress("TooGenericExceptionCaught")
actual fun PlatformContext.externalShareForBitmap(bitmap: ImageBitmap) {
    try {
        val image = bitmap.toUIImage()
        val activityItems = listOf(image)

        val activityViewController = UIActivityViewController(
            activityItems = activityItems,
            applicationActivities = null
        )

        this.presentViewController(
            activityViewController,
            animated = true,
            completion = null
        )
    } catch (e: Exception) {
        Napier.e("[externalShareForBitmap] message: ${e.message}")
    }
}

@Suppress("TooGenericExceptionCaught")
actual fun PlatformContext.bitmapToFileUri(bitmap: ImageBitmap): Uri? {
    try {
        val fileUrl = bitmap.saveToTemporaryFile()
        return fileUrl.absoluteString?.let { Uri.parse(it) }
    } catch (e: Exception) {
        Napier.e("Failed to convert bitmap to URI: ${e.message}")
        return null
    }
}

@Suppress("TooGenericExceptionCaught")
actual fun PlatformContext.shareImage(imageUri: Uri) {
    try {
        val nsUrl = NSURL.URLWithString(imageUri.toString()) ?: return
        val activityItems = listOf(nsUrl)

        val activityViewController = UIActivityViewController(
            activityItems = activityItems,
            applicationActivities = null
        )

        this.presentViewController(
            activityViewController,
            animated = true,
            completion = null
        )
    } catch (e: Exception) {
        Napier.e("Failed to share image: ${e.message}")
    }
}

actual fun ImageBitmap.saveToDisk(context: PlatformContext): String {
    val fileName = "shared_image_${NSDate().timeIntervalSince1970}.png"
    val tempDir = NSTemporaryDirectory()
    val filePath = (tempDir as NSString).stringByAppendingPathComponent(fileName)

    val image = toUIImage()
    UIImagePNGRepresentation(image)?.writeToFile(filePath, true)

    return filePath
}

@Suppress("TooGenericExceptionCaught")
actual fun PlatformContext.saveImageToGallery(bitmap: ImageBitmap) {
    try {
        val image = bitmap.toUIImage()
        PHPhotoLibrary.sharedPhotoLibrary().performChanges({
            PHAssetChangeRequest.creationRequestForAssetFromImage(image)
        }, { success, error ->
            if (success) {
                Napier.d("Successfully saved image to gallery")
            } else {
                Napier.e("Failed to save image to gallery: ${error?.localizedDescription}")
            }
        })
    } catch (e: Exception) {
        Napier.e("Failed to save image to gallery: ${e.message}")
    }
}

@Suppress("TooGenericExceptionCaught")
actual fun PlatformContext.saveUriToGallery(imageUri: Uri) {
    try {
        val nsUrl = NSURL.URLWithString(imageUri.toString()) ?: return
        val imageData = NSData.dataWithContentsOfURL(nsUrl) ?: return
        val image = UIImage.imageWithData(imageData) ?: return

        PHPhotoLibrary.sharedPhotoLibrary().performChanges({
            PHAssetChangeRequest.creationRequestForAssetFromImage(image)
        }, { success, error ->
            if (success) {
                Napier.d("Successfully saved image to gallery")
            } else {
                Napier.e("Failed to save image to gallery: ${error?.localizedDescription}")
            }
        })
    } catch (e: Exception) {
        Napier.e("Failed to save image to gallery: ${e.message}")
    }
}

private fun ImageBitmap.toUIImage(): UIImage {
    // 실제 변환 로직 필요
    return UIImage()
}

private fun ImageBitmap.saveToTemporaryFile(): NSURL {
    val fileName = "temp_image_${NSDate().timeIntervalSince1970}.png"
    val tempDir = NSTemporaryDirectory()
    val filePath = (tempDir as NSString).stringByAppendingPathComponent(fileName)

    val image = toUIImage()
    UIImagePNGRepresentation(image)?.writeToFile(filePath, true)

    return NSURL.fileURLWithPath(filePath)
}

