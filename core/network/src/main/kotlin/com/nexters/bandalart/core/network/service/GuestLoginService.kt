package com.nexters.bandalart.core.network.service

import com.nexters.bandalart.core.network.model.GuestLoginTokenResponse
import retrofit2.http.POST

interface GuestLoginService {
    @POST("v1/users/guests")
    suspend fun createGuestLoginToken(): GuestLoginTokenResponse
}
