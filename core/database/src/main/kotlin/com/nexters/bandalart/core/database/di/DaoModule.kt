package com.nexters.bandalart.core.database.di

import com.nexters.bandalart.core.database.BandalartDao
import com.nexters.bandalart.core.database.BandalartDatabase
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
