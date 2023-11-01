package com.nexters.bandalart.android.core.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.nexters.bandalart.android.core.datastore.DataStoreProvider
import com.nexters.bandalart.android.core.network.BuildConfig
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
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Named

private const val MaxTimeoutMillis = 3000L
private const val MaxRetryCount = 3

private val jsonRule = Json {
  encodeDefaults = true
  ignoreUnknownKeys = true
  prettyPrint = true
  isLenient = true
}

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

  @Singleton
  @Provides
  internal fun provideKtorHttpClient(dataStoreProvider: DataStoreProvider): HttpClient {
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
        json(jsonRule)
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

  @Singleton
  @Provides
  internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor { message ->
      Timber.tag("HttpClient").d(message)
    }.apply {
      level = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor.Level.BODY
      } else {
        HttpLoggingInterceptor.Level.NONE
      }
    }
  }

  @Singleton
  @Provides
  @Named("AuthHttpClient")
  internal fun provideRetrofitAuthHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor,
  ): Retrofit {
    val contentType = "application/json".toMediaType()
    val httpClient = OkHttpClient.Builder()
      .connectTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
      .addInterceptor(httpLoggingInterceptor)
      .build()

    return Retrofit.Builder()
      .baseUrl(BuildConfig.SERVER_BASE_URL)
      .client(httpClient)
      .addConverterFactory(jsonRule.asConverterFactory(contentType))
      .build()
  }

  @Singleton
  @Provides
  @Named("HttpClient")
  internal fun provideRetrofitHttpClient(
    dataStoreProvider: DataStoreProvider,
    httpLoggingInterceptor: HttpLoggingInterceptor,
  ): Retrofit {
    val contentType = "application/json".toMediaType()
    val httpClient = OkHttpClient.Builder()
      .connectTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
      .addInterceptor { chain: Interceptor.Chain ->
        val request = chain.request().newBuilder()
          .addHeader("X-GUEST-KEY", runBlocking { dataStoreProvider.getGuestLoginToken() })
          .build()
        chain.proceed(request)
      }
      .addInterceptor(httpLoggingInterceptor)
      .build()

    return Retrofit.Builder()
      .baseUrl(BuildConfig.SERVER_BASE_URL)
      .client(httpClient)
      .addConverterFactory(jsonRule.asConverterFactory(contentType))
      .build()
  }
}
