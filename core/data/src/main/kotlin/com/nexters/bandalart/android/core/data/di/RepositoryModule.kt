package com.nexters.bandalart.android.core.data.di

import com.nexters.bandalart.android.core.data.repository.GuestLoginTokenRepositoryImpl
import com.nexters.bandalart.android.core.data.repository.RecentBandalartKeyRepositoryImpl
import com.nexters.bandalart.android.core.domain.repository.GuestLoginTokenRepository
import com.nexters.bandalart.android.core.domain.repository.RecentBandalartKeyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

  @Binds
  @Singleton
  abstract fun bindGuestLoginTokenRepository(guestLoginTokenRepositoryImpl: GuestLoginTokenRepositoryImpl): GuestLoginTokenRepository

  @Binds
  @Singleton
  abstract fun bindRecentBandalartKeyRepository(recentBandalartKeyRepositoryImpl: RecentBandalartKeyRepositoryImpl): RecentBandalartKeyRepository
}
