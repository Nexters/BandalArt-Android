package com.nexters.bandalart.android.core.database.di

import com.nexters.bandalart.android.core.database.BandalartDao
import com.nexters.bandalart.android.core.database.BandalartDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun provideBandalartDao(
        database: BandalartDatabase,
    ): BandalartDao = database.bandalartDao()
}
