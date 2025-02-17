package com.nexters.bandalart.core.common

import androidx.compose.ui.graphics.ImageBitmap
import com.eygraber.uri.Uri
import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGBitmapContextCreateImage
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGImageAlphaInfo
import platform.Foundation.NSDate
import platform.Foundation.NSString
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.writeToFile
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.UIKit.UIImageWriteToSavedPhotosAlbum

actual class ImageHandlerProvider {
    @Suppress("TooGenericExceptionCaught")
    actual fun externalShareForBitmap(bitmap: ImageBitmap) {
        try {
            val image = bitmap.toUIImage()
            shareBitmap(image)
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
            shareUrl(nsUrl)
        } catch (e: Exception) {
            Napier.e("Failed to share image: ${e.message}")
        }
    }

    actual fun saveBitmapToDisk(bitmap: ImageBitmap): String {
        val fileName = "shared_image_${NSDate().timeIntervalSince1970}.png"
        val tempDir = NSTemporaryDirectory()
        val filePath = (tempDir as NSString).stringByAppendingPathComponent(fileName)

        val image = bitmap.toUIImage()
        if (image != null) {
            UIImagePNGRepresentation(image)?.writeToFile(filePath, true)
        }

        return filePath
    }

    @OptIn(ExperimentalForeignApi::class)
    @Suppress("TooGenericExceptionCaught")
    actual fun saveImageToGallery(bitmap: ImageBitmap) {
        try {
            val image = bitmap.toUIImage()
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

    @OptIn(ExperimentalForeignApi::class)
    private fun ImageBitmap.toUIImage(): UIImage? {
        val width = this.width
        val height = this.height
        val buffer = IntArray(width * height)
        this.readPixels(buffer)

        // RGBA to BGRA conversion and proper alpha handling
        for (i in buffer.indices) {
            val color = buffer[i]
            val r = (color shr 16) and 0xFF
            val g = (color shr 8) and 0xFF
            val b = color and 0xFF
            val a = (color shr 24) and 0xFF

            // Compose BGRA
            buffer[i] = (a shl 24) or (b shl 16) or (g shl 8) or r
        }

        val colorSpace = CGColorSpaceCreateDeviceRGB()
        val context = CGBitmapContextCreate(
            data = buffer.refTo(0),
            width = width.toULong(),
            height = height.toULong(),
            bitsPerComponent = 8u,
            bytesPerRow = (4 * width).toULong(),
            space = colorSpace,
            bitmapInfo = CGImageAlphaInfo.kCGImageAlphaPremultipliedFirst.value or
                platform.CoreGraphics.kCGBitmapByteOrder32Little
        )

        val cgImage = CGBitmapContextCreateImage(context)
        return cgImage?.let { UIImage.imageWithCGImage(it) }
    }

    private fun shareBitmap(bitmap: UIImage?) {
        bitmap ?: return
        val activityViewController = UIActivityViewController(
            activityItems = listOf(bitmap),
            applicationActivities = null
        )
        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            activityViewController,
            animated = true,
            completion = null
        )
    }

    private fun shareUrl(nsUrl: NSURL) {
        val activityViewController = UIActivityViewController(
            activityItems = listOf(nsUrl),
            applicationActivities = null
        )
        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            activityViewController,
            animated = true,
            completion = null
        )
    }
}
