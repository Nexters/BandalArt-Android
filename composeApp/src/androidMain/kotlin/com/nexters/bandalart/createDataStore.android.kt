package com.nexters.bandalart

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.nexters.bandalart.core.datastore.DATA_STORE_FILE_NAME

fun createDataStore(context: Context): DataStore<Preferences> {
    return com.nexters.bandalart.core.datastore.createDataStore {
        context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    }
}
