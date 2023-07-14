package com.nexters.bandalart.android.data.di

import com.nexters.bandalart.android.data.datasource.GuestLoginTokenDataSource
import com.nexters.bandalart.android.data.local.datasource.GuestLoginTokenDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

  @Binds
  @Singleton
  abstract fun bindGuestLoginTokenDataSource(guestLoginTokenDataSourceImpl: GuestLoginTokenDataSourceImpl): GuestLoginTokenDataSource
}
