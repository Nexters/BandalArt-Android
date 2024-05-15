package com.nexters.bandalart.android.core.datastore.datasource

interface GuestLoginLocalDataSource {
  suspend fun setGuestLoginToken(guestLoginToken: String)
  suspend fun getGuestLoginToken(): String
}
