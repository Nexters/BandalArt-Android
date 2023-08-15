package com.nexters.bandalart.android.core.data.local.datasource

import com.nexters.bandalart.android.core.data.datasource.CompletedBandalartKeyDataSource
import com.nexters.bandalart.android.core.data.local.DataStoreProvider
import javax.inject.Inject

internal class CompletedBandalartKeyDataStoreImpl @Inject constructor(
  private val datastoreProvider: DataStoreProvider,
) : CompletedBandalartKeyDataSource {
  override suspend fun getPrevBandalartList(): List<Pair<String, Boolean>> {
    return datastoreProvider.getPrevBandalartList()
  }

  override suspend fun upsertBandalartKey(bandalartKey: String, isCompleted: Boolean) {
    datastoreProvider.upsertBandalartKey(bandalartKey, isCompleted)
  }

  override suspend fun checkCompletedBandalartKey(bandalartKey: String): Boolean {
    return datastoreProvider.checkCompletedBandalartKey(bandalartKey)
  }

  override suspend fun deleteBandalartKey(bandalartKey: String) {
    datastoreProvider.deleteBandalartKey(bandalartKey)
  }
}
