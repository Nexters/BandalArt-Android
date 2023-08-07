package com.nexters.bandalart.android.core.data.local.datasource

import com.nexters.bandalart.android.core.data.datasource.GuestLoginLocalDataSource
import com.nexters.bandalart.android.core.data.local.DataStoreProvider
import javax.inject.Inject

internal class GuestLoginLocalDataSourceImpl @Inject constructor(private val datastoreProvider: DataStoreProvider) :
  GuestLoginLocalDataSource {

  override suspend fun setGuestLoginToken(guestLoginToken: String) {
    datastoreProvider.setGuestLoginToken(guestLoginToken)
  }

  override suspend fun getGuestLoginToken(): String {
    return datastoreProvider.getGuestLoginToken()
  }
}
