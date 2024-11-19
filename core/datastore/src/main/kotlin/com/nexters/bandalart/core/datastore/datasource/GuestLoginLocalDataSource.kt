package com.nexters.bandalart.core.datastore.datasource

interface GuestLoginLocalDataSource {
    suspend fun setGuestLoginToken(guestLoginToken: String)
    suspend fun getGuestLoginToken(): String
}
