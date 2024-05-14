package com.nexters.bandalart.android.core.network.di

import com.nexters.bandalart.android.core.network.service.BandalartService
import com.nexters.bandalart.android.core.network.service.GuestLoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {

  @Singleton
  @Provides
  internal fun provideBandalartService(
    @BandalartApi retrofit: Retrofit,
  ): BandalartService {
    return retrofit.create(BandalartService::class.java)
  }

  @Singleton
  @Provides
  internal fun provideGuestLoginService(
    @LoginApi retrofit: Retrofit,
  ): GuestLoginService {
    return retrofit.create(GuestLoginService::class.java)
  }
}
