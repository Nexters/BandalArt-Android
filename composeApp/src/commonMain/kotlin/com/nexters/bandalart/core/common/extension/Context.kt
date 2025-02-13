package com.nexters.bandalart.core.common.extension

import androidx.compose.ui.graphics.ImageBitmap
import com.eygraber.uri.Uri

expect class PlatformContext

expect fun PlatformContext.externalShareForBitmap(bitmap: ImageBitmap)

expect fun PlatformContext.bitmapToFileUri(bitmap: ImageBitmap): Uri?

expect fun PlatformContext.shareImage(imageUri: Uri)

expect fun PlatformContext.saveImageToGallery(bitmap: ImageBitmap)

expect fun PlatformContext.saveUriToGallery(imageUri: Uri)

expect fun ImageBitmap.saveToDisk(context: PlatformContext): String
