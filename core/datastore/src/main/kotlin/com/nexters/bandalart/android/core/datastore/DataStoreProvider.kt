package com.nexters.bandalart.android.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
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
        private const val RECENT_BANDALART_ID = "recent_bandalart_id"
        private const val COMPLETED_BANDALART_LIST_ID = "completed_bandalart_list_id"
        private const val ONBOARDING_COMPLETED_ID = "completed_onboarding_id"
    }

    private val prefGuestLoginToken = stringPreferencesKey(GUEST_LOGIN_TOKEN)
    private val prefRecentBandalartId = longPreferencesKey(RECENT_BANDALART_ID)
    private val prefCompletedBandalartList = stringPreferencesKey(COMPLETED_BANDALART_LIST_ID)
    private val prefOnboardingCompletedId = booleanPreferencesKey(ONBOARDING_COMPLETED_ID)

    suspend fun setGuestLoginToken(guestLoginToken: String) {
        dataStore.edit { preferences ->
            preferences[prefGuestLoginToken] = guestLoginToken
        }
    }

    suspend fun getGuestLoginToken() = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.first()[prefGuestLoginToken] ?: ""

    suspend fun setRecentBandalartId(recentBandalartId: Long) {
        dataStore.edit { preferences ->
            preferences[prefRecentBandalartId] = recentBandalartId
        }
    }

    suspend fun getRecentBandalartId() = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.first()[prefRecentBandalartId] ?: 0L

    suspend fun getPrevBandalartList() = stringToList(
        dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }.first()[prefCompletedBandalartList] ?: "",
    )

    // 키가 존재하면 값을 갱신, 없으면 추가
    suspend fun upsertBandalartId(bandalartId: Long, isCompleted: Boolean) {
        dataStore.edit { preferences ->
            val currentListAsString = preferences[prefCompletedBandalartList] ?: ""
            val currentList = stringToList(currentListAsString)
            val isKeyExists = currentList.any { it.first == bandalartId }
            val updatedList = if (isKeyExists) {
                currentList.map {
                    if (it.first == bandalartId) Pair(bandalartId, isCompleted)
                    else it
                }
            } else {
                currentList + Pair(bandalartId, isCompleted)
            }
            preferences[prefCompletedBandalartList] = listToString(updatedList)
        }
    }

    // 목표를 달성하지 못했었는데 이번에 달성한 경우를 검사
    suspend fun checkCompletedBandalartId(bandalartId: Long): Boolean = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.first()[prefCompletedBandalartList]?.let { currentListAsString ->
        val currentList = stringToList(currentListAsString)
        // 이전에 목표를 달성하지 않았었는지 확인
        val wasCompleted = currentList.find { it.first == bandalartId }?.second ?: false
        !wasCompleted
    } ?: false

    suspend fun deleteBandalartId(bandalartId: Long) {
        dataStore.edit { preferences ->
            val currentListAsString = preferences[prefCompletedBandalartList] ?: ""
            val currentList = stringToList(currentListAsString)
            val updatedList = currentList.filter { it.first != bandalartId }
            preferences[prefCompletedBandalartList] = listToString(updatedList)
        }
    }

    private fun listToString(list: List<Pair<Long, Boolean>>): String {
        return Json.encodeToString(list)
    }

    private fun stringToList(data: String): List<Pair<Long, Boolean>> {
        if (data.isEmpty()) return emptyList()
        return Json.decodeFromString(data)
    }

    suspend fun setOnboardingCompletedStatus(flag: Boolean) {
        dataStore.edit { preferences ->
            preferences[prefOnboardingCompletedId] = flag
        }
    }

    suspend fun getOnboardingCompletedStatus() = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.first()[prefOnboardingCompletedId] ?: false
}
