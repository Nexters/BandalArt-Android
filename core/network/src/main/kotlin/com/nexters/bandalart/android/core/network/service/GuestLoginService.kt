package com.nexters.bandalart.android.core.network.service

import com.nexters.bandalart.android.core.network.model.GuestLoginTokenResponse
import retrofit2.http.POST

interface GuestLoginService {
    @POST("v1/users/guests")
    suspend fun createGuestLoginToken(): GuestLoginTokenResponse
}
