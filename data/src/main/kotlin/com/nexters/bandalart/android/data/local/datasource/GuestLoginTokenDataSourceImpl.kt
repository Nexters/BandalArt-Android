package com.nexters.bandalart.android.data.local.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nexters.bandalart.android.data.datasource.GuestLoginTokenDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class GuestLoginTokenDataSourceImpl(private val dataStore: DataStore<Preferences>) : GuestLoginTokenDataSource {

  companion object KEY {
    private const val GUEST_LOGIN_TOKEN = "guest_login_token"
  }

  private val prefKey = stringPreferencesKey(GUEST_LOGIN_TOKEN)

  override suspend fun set(guestLoginToken: String) {
    dataStore.edit { preferences ->
      preferences[prefKey] = guestLoginToken
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
      preference[prefKey]?.let { Result.success(it) }
        ?: Result.failure(NoSuchFieldError("Getting guest login token is failed"))
    }
  }
}
