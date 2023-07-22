package com.nexters.bandalart.android.data.di

import com.nexters.bandalart.android.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Singleton
  @Provides
  fun provideHttpClient(): HttpClient {
    return HttpClient(CIO) {
      defaultRequest {
        // TODO default Header 지정
        url(BuildConfig.SERVER_BASE_URL)
        contentType(ContentType.Application.Json)
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
      install(HttpTimeout) {
        socketTimeoutMillis = 15_000
        requestTimeoutMillis = 15_000
        connectTimeoutMillis = 15_000
      }
    }
  }
}
