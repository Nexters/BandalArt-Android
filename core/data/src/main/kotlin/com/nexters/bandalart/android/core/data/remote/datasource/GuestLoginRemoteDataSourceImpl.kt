package com.nexters.bandalart.android.core.data.remote.datasource

import com.nexters.bandalart.android.core.data.datasource.GuestLoginRemoteDataSource
import com.nexters.bandalart.android.core.data.model.GuestLoginTokenResponse
import com.nexters.bandalart.android.core.data.util.extension.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import javax.inject.Inject

internal class GuestLoginRemoteDataSourceImpl @Inject constructor(
  private val client: HttpClient,
) : GuestLoginRemoteDataSource {
  override suspend fun createGuestLoginToken(): GuestLoginTokenResponse? {
    return client.safeRequest {
      post("v1/users/guests").body()
    }
  }
}
