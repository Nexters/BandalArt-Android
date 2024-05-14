package com.nexters.bandalart.android.core.datastore.di

import com.nexters.bandalart.android.core.datastore.datasource.CompletedBandalartKeyDataSource
import com.nexters.bandalart.android.core.datastore.datasource.GuestLoginLocalDataSource
import com.nexters.bandalart.android.core.datastore.datasource.OnboardingDataSource
import com.nexters.bandalart.android.core.datastore.datasource.RecentBandalartKeyDataSource
import com.nexters.bandalart.android.core.datastore.datasource.CompletedBandalartKeyDataStoreImpl
import com.nexters.bandalart.android.core.datastore.datasource.GuestLoginLocalDataSourceImpl
import com.nexters.bandalart.android.core.datastore.datasource.OnboardingDataSourceImpl
import com.nexters.bandalart.android.core.datastore.datasource.RecentBandalartKeyDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {

  @Binds
  @Singleton
  abstract fun bindGuestLoginLocalDataSource(
    guestLoginLocalDataSourceImpl: GuestLoginLocalDataSourceImpl,
  ): GuestLoginLocalDataSource

  @Binds
  @Singleton
  abstract fun bindRecentBandalartKeyDataSource(
    recentBandalartKeyDataSourceImpl: RecentBandalartKeyDataSourceImpl,
  ): RecentBandalartKeyDataSource

  @Binds
  @Singleton
  abstract fun bindCompletedBandalartKeyDataSource(
    completedBandalartKeyDataStoreImpl: CompletedBandalartKeyDataStoreImpl,
  ): CompletedBandalartKeyDataSource

  @Binds
  @Singleton
  abstract fun bindOnboardingDataSource(
    onboardingDataSourceImpl: OnboardingDataSourceImpl,
  ): OnboardingDataSource
}
