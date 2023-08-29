package com.nexters.bandalart.android.core.data.di

import com.nexters.bandalart.android.core.data.service.BandalartService
import com.nexters.bandalart.android.core.data.service.GuestLoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {

  @Singleton
  @Provides
  internal fun provideBandalartService(retrofit: Retrofit): BandalartService {
    return retrofit.create(BandalartService::class.java)
  }

  @Singleton
  @Provides
  internal fun provideGuestLoginService(retrofit: Retrofit): GuestLoginService {
    return retrofit.create(GuestLoginService::class.java)
  }
}