package com.nexters.bandalart.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.koin.core.module.Module

//fun createDataStore(producePath: () -> String): DataStore<Preferences> {
//    return PreferenceDataStoreFactory.createWithPath(
//        produceFile = { producePath().toPath() }
//    )
//}
//
//internal const val BANDALART_DATA_STORE_FILE_NAME = "bandalart.preferences_pb"
//internal const val IN_APP_UPDATE_DATA_STORE_FILE_NAME = "in_app_update.preferences_pb"

expect class BandalartDataStoreFactory {
    fun createBandalartDataStore(): DataStore<Preferences>
    fun createInAppUpdateDataStore(): DataStore<Preferences>
}

internal const val BANDALART_DATA_STORE_FILE_NAME = "bandalart.preferences_pb"
internal const val IN_APP_UPDATE_DATA_STORE_FILE_NAME = "in_app_update.preferences_pb"
