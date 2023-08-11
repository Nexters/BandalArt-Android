package com.nexters.bandalart.android.core.data.di

import com.nexters.bandalart.android.core.data.datasource.BandalartRemoteDataSource
import com.nexters.bandalart.android.core.data.datasource.CompletedBandalartKeyDataSource
import com.nexters.bandalart.android.core.data.datasource.GuestLoginLocalDataSource
import com.nexters.bandalart.android.core.data.datasource.GuestLoginRemoteDataSource
import com.nexters.bandalart.android.core.data.datasource.RecentBandalartKeyDataSource
import com.nexters.bandalart.android.core.data.local.datasource.CompletedBandalartKeyDataStoreImpl
import com.nexters.bandalart.android.core.data.local.datasource.GuestLoginLocalDataSourceImpl
import com.nexters.bandalart.android.core.data.local.datasource.RecentBandalartKeyDataSourceImpl
import com.nexters.bandalart.android.core.data.remote.datasource.BandalartRemoteDataSourceImpl
import com.nexters.bandalart.android.core.data.remote.datasource.GuestLoginRemoteDataSourceImpl
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
  abstract fun bindGuestLoginLocalDataSource(guestLoginLocalDataSourceImpl: GuestLoginLocalDataSourceImpl): GuestLoginLocalDataSource

  @Binds
  @Singleton
  abstract fun bindGuestLoginRemoteDataSource(guestLoginRemoteDataSourceImpl: GuestLoginRemoteDataSourceImpl): GuestLoginRemoteDataSource

  @Binds
  @Singleton
  abstract fun bindRecentBandalartKeyDataSource(recentBandalartKeyDataSourceImpl: RecentBandalartKeyDataSourceImpl): RecentBandalartKeyDataSource

  @Binds
  @Singleton
  abstract fun bindBandalartRemoteDataSource(bandalartRemoteDataSourceImpl: BandalartRemoteDataSourceImpl): BandalartRemoteDataSource

  @Binds
  @Singleton
  abstract fun bindCompletedBandalartKeyDataSource(completedBandalartKeyDataStoreImpl: CompletedBandalartKeyDataStoreImpl): CompletedBandalartKeyDataSource
}
