package com.nexters.bandalart.android.core.data.datasource

interface CompletedBandalartKeyDataSource {
  suspend fun getPrevBandalartList(): List<Pair<String, Boolean>>
  suspend fun upsertBandalartKey(bandalartKey: String, isCompleted: Boolean)
  suspend fun checkCompletedBandalartKey(bandalartKey: String): Boolean
  suspend fun deleteBandalartKey(bandalartKey: String)
}
