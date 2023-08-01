@file:OptIn(InternalAPI::class)

package com.nexters.bandalart.android.core.data.remote.datasource

import com.nexters.bandalart.android.core.data.datasource.BandalartRemoteDataSource
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartCellResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartDetailResponse
import com.nexters.bandalart.android.core.data.model.bandalart.BandalartListResponse
import com.nexters.bandalart.android.core.data.model.bandalart.UpdateBandalartCellRequest
import com.nexters.bandalart.android.core.data.util.ExceptionWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.util.InternalAPI
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

// TODO 반복 되는 코드 모듈화 필요성
@Suppress("TooGenericExceptionCaught")
class BandalartRemoteDataSourceImpl @Inject constructor(
  private val client: HttpClient,
) : BandalartRemoteDataSource {
  override suspend fun createBandalart() {
    try {
      client
        .post("v1/bandalarts")
    } catch (exception: UnresolvedAddressException) {
      throw ExceptionWrapper(message = "No Internet connection", cause = exception)
    } catch (exception: ResponseException) {
      throw ExceptionWrapper(
        statusCode = exception.response.status.value,
        message = exception.response.status.description,
        cause = exception,
      )
    } catch (exception: Exception) {
      throw ExceptionWrapper(message = "Unexpected Error", cause = exception)
    }
  }

  override suspend fun getBandalartList(): BandalartListResponse? {
    return try {
      client
        .get("v1/bandalarts")
        .body()
    } catch (exception: UnresolvedAddressException) {
      throw ExceptionWrapper(message = "No Internet connection", cause = exception)
    } catch (exception: ResponseException) {
      throw ExceptionWrapper(
        statusCode = exception.response.status.value,
        message = exception.response.status.description,
        cause = exception,
      )
    } catch (exception: Exception) {
      throw ExceptionWrapper(message = "Unexpected Error", cause = exception)
    }
  }

  override suspend fun getBandalartDetail(bandalartKey: String): BandalartDetailResponse? {
    return try {
      client
        .get("v1/bandalarts/$bandalartKey")
        .body()
    } catch (exception: UnresolvedAddressException) {
      throw ExceptionWrapper(message = "No Internet connection", cause = exception)
    } catch (exception: ResponseException) {
      throw ExceptionWrapper(
        statusCode = exception.response.status.value,
        message = exception.response.status.description,
        cause = exception,
      )
    } catch (exception: Exception) {
      throw ExceptionWrapper(message = "Unexpected Error", cause = exception)
    }
  }

  override suspend fun deleteBandalart(bandalartKey: String) {
    try {
      client
        .delete("v1/bandalarts/$bandalartKey")
    } catch (exception: UnresolvedAddressException) {
      throw ExceptionWrapper(message = "No Internet connection", cause = exception)
    } catch (exception: ResponseException) {
      throw ExceptionWrapper(
        statusCode = exception.response.status.value,
        message = exception.response.status.description,
        cause = exception,
      )
    } catch (exception: Exception) {
      throw ExceptionWrapper(message = "Unexpected Error", cause = exception)
    }
  }

  override suspend fun getBandalartMainCell(bandalartKey: String): BandalartCellResponse? {
    return try {
      client
        .get("v1/bandalarts/$bandalartKey/cells")
        .body()
    } catch (exception: UnresolvedAddressException) {
      throw ExceptionWrapper(message = "No Internet connection", cause = exception)
    } catch (exception: ResponseException) {
      throw ExceptionWrapper(
        statusCode = exception.response.status.value,
        message = exception.response.status.description,
        cause = exception,
      )
    } catch (exception: Exception) {
      throw ExceptionWrapper(message = "Unexpected Error", cause = exception)
    }
  }

  override suspend fun getBandalartCell(bandalartKey: String, cellKey: String): BandalartCellResponse? {
    return try {
      client
        .get("v1/bandalarts/$bandalartKey/cells/$cellKey")
        .body()
    } catch (exception: UnresolvedAddressException) {
      throw ExceptionWrapper(message = "No Internet connection", cause = exception)
    } catch (exception: ResponseException) {
      throw ExceptionWrapper(
        statusCode = exception.response.status.value,
        message = exception.response.status.description,
        cause = exception,
      )
    } catch (exception: Exception) {
      throw ExceptionWrapper(message = "Unexpected Error", cause = exception)
    }
  }

  override suspend fun updateBandalartCell(
    bandalartKey: String,
    cellKey: String,
    updateBandalartRequest: UpdateBandalartCellRequest,
  ) {
    try {
      client
        .patch("v1/bandalarts/$bandalartKey/cells/$cellKey") {
          setBody(updateBandalartRequest)
        }
    } catch (exception: UnresolvedAddressException) {
      throw ExceptionWrapper(message = "No Internet connection", cause = exception)
    } catch (exception: ResponseException) {
      throw ExceptionWrapper(
        statusCode = exception.response.status.value,
        message = exception.response.status.description,
        cause = exception,
      )
    } catch (exception: Exception) {
      throw ExceptionWrapper(message = "Unexpected Error", cause = exception)
    }
  }

  override suspend fun deleteBandalartCell(bandalartKey: String, cellKey: String) {
    try {
      client
        .delete("v1/bandalarts/$bandalartKey/cells/$cellKey")
    } catch (exception: UnresolvedAddressException) {
      throw ExceptionWrapper(message = "No Internet connection", cause = exception)
    } catch (exception: ResponseException) {
      throw ExceptionWrapper(
        statusCode = exception.response.status.value,
        message = exception.response.status.description,
        cause = exception,
      )
    } catch (exception: Exception) {
      throw ExceptionWrapper(message = "Unexpected Error", cause = exception)
    }
  }
}
