package com.nexters.bandalart.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.nexters.bandalart.core.datastore.DATA_STORE_FILE_NAME
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import com.nexters.bandalart.core.datastore.createDataStore

@OptIn(ExperimentalForeignApi::class)
fun createDataStore(): DataStore<Preferences> = createDataStore {
    val directory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    requireNotNull(directory).path + "/$DATA_STORE_FILE_NAME"
}
