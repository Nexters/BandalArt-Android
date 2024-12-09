package com.nexters.bandalart.core.datastore.di

import com.nexters.bandalart.core.datastore.datasource.GuestLoginLocalDataSource
import com.nexters.bandalart.core.datastore.datasource.OnboardingDataSource
import com.nexters.bandalart.core.datastore.datasource.GuestLoginLocalDataSourceImpl
import com.nexters.bandalart.core.datastore.datasource.OnboardingDataSourceImpl
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
    abstract fun bindOnboardingDataSource(
        onboardingDataSourceImpl: OnboardingDataSourceImpl,
    ): OnboardingDataSource
}
