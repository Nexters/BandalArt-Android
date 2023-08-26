package com.nexters.bandalart.android.core.data.di

import com.nexters.bandalart.android.core.data.BuildConfig
import com.nexters.bandalart.android.core.data.local.DataStoreProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import timber.log.Timber

private const val MaxTimeoutMillis = 3000L
private const val MaxRetryCount = 3

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

  @Singleton
  @Provides
  fun provideKtorHttpClient(dataStoreProvider: DataStoreProvider): HttpClient {
    return HttpClient(engineFactory = CIO) {
      engine {
        endpoint {
          connectTimeout = MaxTimeoutMillis
          connectAttempts = MaxRetryCount
        }
      }
      defaultRequest {
        val guestLoginToken = runBlocking {
          dataStoreProvider.getGuestLoginToken()
        }
        url(BuildConfig.SERVER_BASE_URL)
        contentType(ContentType.Application.Json)
        header("X-GUEST-KEY", guestLoginToken)
      }
      install(ContentNegotiation) {
        json(Json {
          encodeDefaults = true
          ignoreUnknownKeys = true
          prettyPrint = true
          isLenient = true
        })
      }
      install(Logging) {
        logger = object : Logger {
          override fun log(message: String) {
            Timber.tag("HttpClient").d(message)
          }
        }
        level = LogLevel.ALL
      }
    }
  }
}
