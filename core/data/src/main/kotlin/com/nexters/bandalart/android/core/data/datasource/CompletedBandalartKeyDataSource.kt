package com.nexters.bandalart.android.core.data.datasource

interface CompletedBandalartKeyDataSource {
  suspend fun insertCompletedBandalartKey(bandalartKey: String)
  suspend fun checkCompletedBandalartKey(bandalartKey: String): Boolean
  suspend fun deleteCompletedBandalartKey(bandalartKey: String)
}
