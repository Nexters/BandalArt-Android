package com.nexters.bandalart.core.common

import androidx.compose.ui.graphics.ImageBitmap
import com.eygraber.uri.Uri
import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDate
import platform.Foundation.NSString
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.writeToFile
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.UIKit.UIImageWriteToSavedPhotosAlbum
import platform.UIKit.UIViewController

actual class ImageHandlerProvider {
    @Suppress("TooGenericExceptionCaught")
    actual fun externalShareForBitmap(bitmap: ImageBitmap) {
        try {
            val image = bitmap.toUIImage()
            shareItems(listOf(image))
        } catch (e: Exception) {
            Napier.e("[externalShareFoBitmap] message: ${e.message}")
        }
    }

    @Suppress("TooGenericExceptionCaught")
    actual fun bitmapToFileUri(bitmap: ImageBitmap): Uri? {
        return try {
            val filePath = saveBitmapToDisk(bitmap)
            NSURL.fileURLWithPath(filePath).absoluteString?.let { Uri.parse(it) }
        } catch (e: Exception) {
            Napier.e("Failed to convert bitmap to URI: ${e.message}")
            null
        }
    }

    @Suppress("TooGenericExceptionCaught")
    actual fun shareImage(imageUri: Uri) {
        try {
            val nsUrl = NSURL.URLWithString(imageUri.toString()) ?: return
            shareItems(listOf(nsUrl))
        } catch (e: Exception) {
            Napier.e("Failed to share image: ${e.message}")
        }
    }

    actual fun saveBitmapToDisk(bitmap: ImageBitmap): String {
        val fileName = "shared_image_${NSDate().timeIntervalSince1970}.png"
        val tempDir = NSTemporaryDirectory()
        val filePath = (tempDir as NSString).stringByAppendingPathComponent(fileName)

        val image = bitmap.toUIImage()
        UIImagePNGRepresentation(image)?.writeToFile(filePath, true)

        return filePath
    }

    @OptIn(ExperimentalForeignApi::class)
    @Suppress("TooGenericExceptionCaught")
    actual fun saveImageToGallery(bitmap: ImageBitmap) {
        try {
            val image = bitmap.toUIImage()
            UIImageWriteToSavedPhotosAlbum(
                image,
                null,
                null,
                null
            )
        } catch (e: Exception) {
            Napier.e("Failed to save image to gallery: ${e.message}")
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    @Suppress("TooGenericExceptionCaught")
    actual fun saveUriToGallery(imageUri: Uri) {
        try {
            val nsUrl = NSURL.URLWithString(imageUri.toString()) ?: return
            val image = UIImage.imageWithContentsOfFile(nsUrl.path!!)
            if (image != null) {
                UIImageWriteToSavedPhotosAlbum(
                    image,
                    null,
                    null,
                    null
                )
            }
        } catch (e: Exception) {
            Napier.e("Failed to save image to gallery: ${e.message}")
        }
    }

    private fun shareItems(items: List<Any>) {
        val viewController = getMainViewController()
        val activityViewController = UIActivityViewController(
            activityItems = items,
            applicationActivities = null
        )
        viewController.presentViewController(
            activityViewController,
            animated = true,
            completion = null
        )
    }

    private fun ImageBitmap.toUIImage(): UIImage {
        // 실제 UIImage 변환 구현 필요
        return UIImage()
    }

    private fun getMainViewController(): UIViewController {
        // 실제 메인 ViewController 획득 로직 구현 필요
        return UIViewController()
    }
}
