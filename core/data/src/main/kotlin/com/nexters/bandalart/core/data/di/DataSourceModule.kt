package com.nexters.bandalart.core.data.di

import com.nexters.bandalart.core.data.datasource.BandalartRemoteDataSource
import com.nexters.bandalart.core.data.datasource.GuestLoginRemoteDataSource
import com.nexters.bandalart.core.data.datasource.BandalartRemoteDataSourceImpl
import com.nexters.bandalart.core.data.datasource.GuestLoginRemoteDataSourceImpl
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
    abstract fun bindGuestLoginRemoteDataSource(
        guestLoginRemoteDataSourceImpl: GuestLoginRemoteDataSourceImpl,
    ): GuestLoginRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindBandalartRemoteDataSource(
        bandalartRemoteDataSourceImpl: BandalartRemoteDataSourceImpl,
    ): BandalartRemoteDataSource
}
