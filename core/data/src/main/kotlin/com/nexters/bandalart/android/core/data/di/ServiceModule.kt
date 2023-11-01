package com.nexters.bandalart.android.core.data.di

import com.nexters.bandalart.android.core.data.service.BandalartService
import com.nexters.bandalart.android.core.data.service.GuestLoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {

  @Singleton
  @Provides
  internal fun provideBandalartService(
    @Named("HttpClient")
    retrofit: Retrofit,
  ): BandalartService {
    return retrofit.create(BandalartService::class.java)
  }

  @Singleton
  @Provides
  internal fun provideGuestLoginService(
    @Named("AuthHttpClient")
    retrofit: Retrofit,
  ): GuestLoginService {
    return retrofit.create(GuestLoginService::class.java)
  }
}
