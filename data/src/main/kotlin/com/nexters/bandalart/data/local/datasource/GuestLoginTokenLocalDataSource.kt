package com.nexters.bandalart.data.local.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nexters.bandalart.data.datasource.GuestLoginTokenDataSource
import com.nexters.bandalart.data.local.datastore.DataStoreKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class GuestLoginTokenLocalDataSource(private val dataStore: DataStore<Preferences>) : GuestLoginTokenDataSource {

  private val key = stringPreferencesKey(DataStoreKeys.GUEST_LOGIN_TOKEN)

  override suspend fun set(guestLoginToken: String) {
    dataStore.edit { preferences ->
      preferences[key] = guestLoginToken
    }
  }

  override suspend fun get(): Flow<Result<String>> {
    return dataStore.data.catch { exception ->
      if (exception is IOException) {
        emit(emptyPreferences())
      } else {
        throw exception
      }
    }.map { preference ->
      preference[key]?.let { Result.success(it) }
        ?: Result.failure(NoSuchFieldError("Getting guest login token is failed"))
    }
  }
}
