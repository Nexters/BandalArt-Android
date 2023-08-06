package com.nexters.bandalart.android.core.data.datasource

interface GuestLoginTokenDataSource {
  suspend fun setGuestLoginToken(guestLoginToken: String)
  suspend fun getGuestLoginToken(): String
}
