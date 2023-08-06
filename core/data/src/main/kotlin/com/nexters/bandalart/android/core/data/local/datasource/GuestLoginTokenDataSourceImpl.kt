package com.nexters.bandalart.android.core.data.local.datasource

import com.nexters.bandalart.android.core.data.datasource.GuestLoginTokenDataSource
import com.nexters.bandalart.android.core.data.local.DataStoreProvider
import javax.inject.Inject

class GuestLoginTokenDataSourceImpl @Inject constructor(private val datastoreProvider: DataStoreProvider) :
  GuestLoginTokenDataSource {

  override suspend fun setGuestLoginToken(guestLoginToken: String) {
    datastoreProvider.setGuestLoginToken(guestLoginToken)
  }

  override suspend fun getGuestLoginToken(): String {
    return datastoreProvider.getGuestLoginToken()
  }
}
