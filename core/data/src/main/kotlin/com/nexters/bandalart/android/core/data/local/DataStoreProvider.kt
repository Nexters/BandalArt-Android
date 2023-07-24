package com.nexters.bandalart.android.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class DataStoreProvider @Inject constructor(private val dataStore: DataStore<Preferences>) {

  companion object {
    private const val GUEST_LOGIN_TOKEN = "guest_login_token"
    private const val RECENT_BANDALART_KEY = "recent_bandalart_key"
  }

  private val prefKeyGuestLoginToken = stringPreferencesKey(GUEST_LOGIN_TOKEN)
  private val prefKeyRecentBandalartKey = stringPreferencesKey(RECENT_BANDALART_KEY)

  suspend fun setGuestLoginToken(guestLoginToken: String) {
    dataStore.edit { preferences ->
      preferences[prefKeyGuestLoginToken] = guestLoginToken
    }
  }

  fun getGuestLoginToken(): Flow<String> {
    return dataStore.data.catch { exception ->
      if (exception is IOException) {
        emit(emptyPreferences())
      } else {
        throw exception
      }
    }.map { preference ->
      preference[prefKeyGuestLoginToken] ?: "" }
  }

  suspend fun setRecentBandalartKey(recentBandalartKey: String) {
    dataStore.edit { preferences ->
      preferences[prefKeyRecentBandalartKey] = recentBandalartKey
    }
  }

  fun getRecentBandalartKey(): Flow<String> {
    return dataStore.data.catch { exception ->
      if (exception is IOException) {
        emit(emptyPreferences())
      } else {
        throw exception
      }
    }.map { preference ->
      preference[prefKeyRecentBandalartKey] ?: "" }
  }
}
