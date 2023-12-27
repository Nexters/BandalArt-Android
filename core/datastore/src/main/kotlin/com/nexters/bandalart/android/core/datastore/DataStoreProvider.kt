package com.nexters.bandalart.android.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DataStoreProvider @Inject constructor(
  private val dataStore: DataStore<Preferences>,
) {

  private companion object {
    private const val GUEST_LOGIN_TOKEN = "guest_login_token"
    private const val RECENT_BANDALART_KEY = "recent_bandalart_key"
    private const val COMPLETED_BANDALART_LIST_KEY = "completed_bandalart_list_key"
    private const val ONBOARDING_COMPLETED_KEY = "completed_onboarding_key"
  }

  private val prefKeyGuestLoginToken = stringPreferencesKey(GUEST_LOGIN_TOKEN)
  private val prefKeyRecentBandalartKey = stringPreferencesKey(RECENT_BANDALART_KEY)
  private val prefKeyCompletedBandalartList = stringPreferencesKey(COMPLETED_BANDALART_LIST_KEY)
  private val prefKeyOnboardingCompletedKey = booleanPreferencesKey(ONBOARDING_COMPLETED_KEY)

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

  suspend fun getPrevBandalartList() = stringToList(
    dataStore.data
      .catch { exception ->
        if (exception is IOException) emit(emptyPreferences())
        else throw exception
      }.first()[prefKeyCompletedBandalartList] ?: "",
  )

  // 키가 존재하면 값을 갱신, 없으면 추가
  suspend fun upsertBandalartKey(bandalartKey: String, isCompleted: Boolean) {
    dataStore.edit { preferences ->
      val currentListAsString = preferences[prefKeyCompletedBandalartList] ?: ""
      val currentList = stringToList(currentListAsString)
      val isKeyExists = currentList.any { it.first == bandalartKey }
      val updatedList = if (isKeyExists) {
        currentList.map {
          if (it.first == bandalartKey) Pair(bandalartKey, isCompleted)
          else it
        }
      } else {
        currentList + Pair(bandalartKey, isCompleted)
      }
      preferences[prefKeyCompletedBandalartList] = listToString(updatedList)
    }
  }

  // 목표를 달성하지 못했었는데 이번에 달성한 경우를 검사
  suspend fun checkCompletedBandalartKey(bandalartKey: String): Boolean = dataStore.data
    .catch { exception ->
      if (exception is IOException) emit(emptyPreferences())
      else throw exception
    }.first()[prefKeyCompletedBandalartList]?.let { currentListAsString ->
    val currentList = stringToList(currentListAsString)
    // 이전에 목표를 달성하지 않았었는지 확인
    val wasCompleted = currentList.find { it.first == bandalartKey }?.second ?: false
    !wasCompleted
  } ?: false

  suspend fun deleteBandalartKey(bandalartKey: String) {
    dataStore.edit { preferences ->
      val currentListAsString = preferences[prefKeyCompletedBandalartList] ?: ""
      val currentList = stringToList(currentListAsString)
      val updatedList = currentList.filter { it.first != bandalartKey }
      preferences[prefKeyCompletedBandalartList] = listToString(updatedList)
    }
  }

  private fun listToString(list: List<Pair<String, Boolean>>): String {
    return Json.encodeToString(list)
  }

  private fun stringToList(data: String): List<Pair<String, Boolean>> {
    if (data.isEmpty()) return emptyList()
    return Json.decodeFromString(data)
  }

  suspend fun setOnboardingCompletedStatus(flag: Boolean) {
    dataStore.edit { preferences ->
      preferences[prefKeyOnboardingCompletedKey] = flag
    }
  }

  suspend fun getOnboardingCompletedStatus() = dataStore.data
    .catch { exception ->
      if (exception is IOException) emit(emptyPreferences())
      else throw exception
    }.first()[prefKeyOnboardingCompletedKey] ?: false
}
