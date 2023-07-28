package com.nexters.bandalart.android.core.data.di

import com.nexters.bandalart.android.core.data.datasource.BandalartRemoteDataSource
import com.nexters.bandalart.android.core.data.datasource.GuestLoginTokenDataSource
import com.nexters.bandalart.android.core.data.datasource.RecentBandalartKeyDataSource
import com.nexters.bandalart.android.core.data.local.datasource.GuestLoginTokenDataSourceImpl
import com.nexters.bandalart.android.core.data.local.datasource.RecentBandalartKeyDataSourceImpl
import com.nexters.bandalart.android.core.data.remote.datasource.BandalartRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

  @Binds
  @Singleton
  abstract fun bindGuestLoginTokenDataSource(guestLoginTokenDataSourceImpl: GuestLoginTokenDataSourceImpl): GuestLoginTokenDataSource

  @Binds
  @Singleton
  abstract fun bindRecentBandalartKeyDataSource(recentBandalartKeyDataSourceImpl: RecentBandalartKeyDataSourceImpl): RecentBandalartKeyDataSource

  @Binds
  @Singleton
  abstract fun bindBandalartRemoteDataSource(bandalartRemoteDataSourceImpl: BandalartRemoteDataSourceImpl): BandalartRemoteDataSource
}
