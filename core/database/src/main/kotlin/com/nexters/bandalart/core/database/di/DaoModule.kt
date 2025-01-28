package com.nexters.bandalart.core.database.di

import android.content.Context
import androidx.room.Room
import com.nexters.bandalart.core.database.BandalartDao
import com.nexters.bandalart.core.database.BandalartDatabase
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

//@Module
//@InstallIn(SingletonComponent::class)
//object DaoModule {
//    @Provides
//    fun provideBandalartDao(
//        database: BandalartDatabase,
//    ): BandalartDao = database.bandalartDao()
//}

@ContributesTo(AppScope::class)
interface DatabaseComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideBandalartDatabase(context: Context): BandalartDatabase =
        Room.databaseBuilder(
            context,
            BandalartDatabase::class.java,
            "bandalart_database",
        ).build()

    @Provides
    @SingleIn(AppScope::class)
    fun provideBandalartDao(database: BandalartDatabase): BandalartDao = database.bandalartDao()
}
