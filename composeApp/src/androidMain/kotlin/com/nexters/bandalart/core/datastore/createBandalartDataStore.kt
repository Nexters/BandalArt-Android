package com.nexters.bandalart.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.nexters.bandalart.core.datastore.createDataStore

fun dataStore(context: Context): DataStore<Preferences> =
    createDataStore(
        producePath = { context.filesDir.resolve("bandalart.preferences_pb").absolutePath }
    )
