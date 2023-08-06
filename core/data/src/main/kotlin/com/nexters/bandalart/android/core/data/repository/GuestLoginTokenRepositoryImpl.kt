package com.nexters.bandalart.android.core.data.repository

import com.nexters.bandalart.android.core.data.datasource.GuestLoginTokenDataSource
import com.nexters.bandalart.android.core.domain.repository.GuestLoginTokenRepository
import javax.inject.Inject

class GuestLoginTokenRepositoryImpl @Inject constructor(private val dataSource: GuestLoginTokenDataSource) :
  GuestLoginTokenRepository {
  override suspend fun setGuestLoginToken(guestLoginToken: String) {
    dataSource.setGuestLoginToken(guestLoginToken)
  }

  override suspend fun getGuestLoginToken(): String {
    return dataSource.getGuestLoginToken()
  }
}
