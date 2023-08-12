package com.nexters.bandalart.android.core.data.local.datasource

import com.nexters.bandalart.android.core.data.datasource.CompletedBandalartKeyDataSource
import com.nexters.bandalart.android.core.data.local.DataStoreProvider
import javax.inject.Inject

internal class CompletedBandalartKeyDataStoreImpl @Inject constructor(
  private val datastoreProvider: DataStoreProvider,
) : CompletedBandalartKeyDataSource {
  override suspend fun insertCompletedBandalartKey(bandalartKey: String) {
    datastoreProvider.insertCompletedBandalartKey(bandalartKey)
  }

  override suspend fun checkCompletedBandalartKey(bandalartKey: String): Boolean {
    return datastoreProvider.checkCompletedBandalartKey(bandalartKey)
  }

  override suspend fun deleteCompletedBandalartKey(bandalartKey: String) {
    datastoreProvider.deleteCompletedBandalartKey(bandalartKey)
  }
}
