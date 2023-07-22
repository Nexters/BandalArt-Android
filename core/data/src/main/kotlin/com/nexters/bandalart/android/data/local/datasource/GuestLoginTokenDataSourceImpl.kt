package com.nexters.bandalart.android.data.local.datasource

import com.nexters.bandalart.android.data.datasource.GuestLoginTokenDataSource
import com.nexters.bandalart.android.data.local.DataStoreProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GuestLoginTokenDataSourceImpl @Inject constructor(private val datastoreProvider: DataStoreProvider) : GuestLoginTokenDataSource {

  override suspend fun setGuestLoginToken(guestLoginToken: String) {
    datastoreProvider.setGuestLoginToken(guestLoginToken)
  }

  override fun getGuestLoginToken(): Flow<Result<String>> {
    return datastoreProvider.getGuestLoginToken()
  }
}
