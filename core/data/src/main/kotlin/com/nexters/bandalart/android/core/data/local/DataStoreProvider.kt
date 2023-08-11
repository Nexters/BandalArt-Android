package com.nexters.bandalart.android.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class DataStoreProvider @Inject constructor(
  private val dataStore: DataStore<Preferences>,
) {

  companion object {
    private const val GUEST_LOGIN_TOKEN = "guest_login_token"
    private const val RECENT_BANDALART_KEY = "recent_bandalart_key"
    private const val COMPLETED_BANDALART_LIST_KEY = "completed_bandalart_list_key"
  }

  private val prefKeyGuestLoginToken = stringPreferencesKey(GUEST_LOGIN_TOKEN)
  private val prefKeyRecentBandalartKey = stringPreferencesKey(RECENT_BANDALART_KEY)
  private val prefKeyCompletedBandalartList = stringPreferencesKey(COMPLETED_BANDALART_LIST_KEY)

  suspend fun setGuestLoginToken(guestLoginToken: String) {
    dataStore.edit { preferences ->
      preferences[prefKeyGuestLoginToken] = guestLoginToken
    }
  }

  suspend fun getGuestLoginToken() = dataStore.data
    .catch { exception ->
      if (exception is IOException) emit(emptyPreferences())
      else throw exception
    }.first()[prefKeyGuestLoginToken] ?: ""

  suspend fun setRecentBandalartKey(recentBandalartKey: String) {
    dataStore.edit { preferences ->
      preferences[prefKeyRecentBandalartKey] = recentBandalartKey
    }
  }

  suspend fun getRecentBandalartKey() = dataStore.data
    .catch { exception ->
      if (exception is IOException) emit(emptyPreferences())
      else throw exception
    }.first()[prefKeyRecentBandalartKey] ?: ""

  suspend fun insertCompletedBandalartKey(bandalartKey: String) {
    dataStore.edit { preferences ->
      val currentListAsString = preferences[prefKeyCompletedBandalartList] ?: ""
      val currentList = stringToList(currentListAsString)

      // 키가 이미 존재하지 않는 경우에만 추가
      if (!currentList.contains(bandalartKey)) {
        val updatedList = currentList + bandalartKey
        preferences[prefKeyCompletedBandalartList] = listToString(updatedList)
      }
    }
  }

  suspend fun checkCompletedBandalartKey(bandalartKey: String) = dataStore.data
    .catch { exception ->
      if (exception is IOException) emit(emptyPreferences())
      else throw exception
    }.first()[prefKeyCompletedBandalartList]?.let { currentListAsString ->
    val currentList = stringToList(currentListAsString)
    currentList.contains(bandalartKey)
  } ?: false

  suspend fun deleteCompletedBandalartKey(bandalartKey: String) {
    dataStore.edit { preferences ->
      val currentListAsString = preferences[prefKeyCompletedBandalartList] ?: ""
      val currentList = stringToList(currentListAsString)

      // 키가 존재하는 경우에만 제거
      if (currentList.contains(bandalartKey)) {
        val updatedList = currentList - bandalartKey
        preferences[prefKeyCompletedBandalartList] = listToString(updatedList)
      }
    }
  }

  private fun listToString(list: List<String>): String {
    return Json.encodeToString(list)
  }

  private fun stringToList(data: String): List<String> {
    if (data.isEmpty()) return emptyList()
    return Json.decodeFromString(data)
  }
}
