package com.nexters.bandalart.core.database.di

import android.content.Context
import androidx.room.Room
import com.nexters.bandalart.core.database.BandalartDatabase
import org.koin.dsl.module

// @Module
// @InstallIn(SingletonComponent::class)
// object DatabaseModule {
//
//     @Singleton
//     @Provides
//     fun provideBandalartDatabase(@ApplicationContext context: Context): BandalartDatabase =
//         Room.databaseBuilder(
//             context.applicationContext,
//             BandalartDatabase::class.java,
//             "bandalart_database",
//         ).build()
// }

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get<Context>().applicationContext,
            BandalartDatabase::class.java,
            "bandalart_database",
        ).build()
    }
}
