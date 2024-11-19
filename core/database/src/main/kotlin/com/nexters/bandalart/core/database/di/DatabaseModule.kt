package com.nexters.bandalart.core.database.di

import android.content.Context
import androidx.room.Room
import com.nexters.bandalart.core.database.BandalartDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideBandalartDatabase(@ApplicationContext context: Context): BandalartDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            BandalartDatabase::class.java,
            "bandalart_database",
        ).build()
}
