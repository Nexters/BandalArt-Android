package com.nexters.bandalart.android.core.data.datasource

interface GuestLoginLocalDataSource {
  suspend fun setGuestLoginToken(guestLoginToken: String)
  suspend fun getGuestLoginToken(): String
}
