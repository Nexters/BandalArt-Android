package com.nexters.bandalart.android.core.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.nexters.bandalart.android.core.data.BuildConfig
import com.nexters.bandalart.android.core.data.local.DataStoreProvider
import com.nexters.bandalart.android.core.data.service.BandalartService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

private const val MaxTimeoutMillis = 3000L
// private const val MaxRetryCount = 3

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

//  @Singleton
//  @Provides
//  fun provideKtorHttpClient(dataStoreProvider: DataStoreProvider): HttpClient {
//    return HttpClient(engineFactory = CIO) {
//      engine {
//        endpoint {
//          connectTimeout = MaxTimeoutMillis
//          connectAttempts = MaxRetryCount
//        }
//      }
//      defaultRequest {
//        val guestLoginToken = runBlocking {
//          dataStoreProvider.getGuestLoginToken()
//        }
//        url(BuildConfig.SERVER_BASE_URL)
//        contentType(ContentType.Application.Json)
//        header("X-GUEST-KEY", guestLoginToken)
//      }
//      install(ContentNegotiation) {
//        json(Json {
//          encodeDefaults = true
//          ignoreUnknownKeys = true
//          prettyPrint = true
//          isLenient = true
//        })
//      }
//      install(Logging) {
//        logger = object : Logger {
//          override fun log(message: String) {
//            Timber.tag("HttpClient").d(message)
//          }
//        }
//        level = LogLevel.ALL
//      }
//    }
//  }

  @Singleton
  @Provides
  fun provideRetrofitHttpClient(
    dataStoreProvider: DataStoreProvider,
  ): Retrofit {
    val contentType = "application/json".toMediaType()
    val httpClient = OkHttpClient.Builder()
      .connectTimeout(MaxTimeoutMillis, TimeUnit.MILLISECONDS)
      .addInterceptor { chain: Interceptor.Chain ->
        val request = chain.request().newBuilder()
          .addHeader("Content-Type", "application/json")
          .addHeader("X-GUEST-KEY", runBlocking { dataStoreProvider.getGuestLoginToken() })
          .build()
        chain.proceed(request)
      }
      .addInterceptor(
        HttpLoggingInterceptor { message ->
          Timber.tag("HttpClient").d(message)
        }.setLevel(HttpLoggingInterceptor.Level.BODY),
      )
      .build()

    return Retrofit.Builder()
      .baseUrl(BuildConfig.SERVER_BASE_URL)
      .client(httpClient)
      .addConverterFactory(Json.asConverterFactory(contentType))
      .build()
  }

  @Singleton
  @Provides
  internal fun provideBandalartService(retrofit: Retrofit): BandalartService {
    return retrofit.create(BandalartService::class.java)
  }
}
