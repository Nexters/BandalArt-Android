package com.nexters.bandalart.core.database.di

import com.nexters.bandalart.core.database.BandalartDatabase
import org.koin.dsl.module

// @Module
// @InstallIn(SingletonComponent::class)
// object DaoModule {
//     @Provides
//     fun provideBandalartDao(
//         database: BandalartDatabase,
//     ): BandalartDao = database.bandalartDao()
// }

val daoModule = module {
    includes(databaseModule)

    single { get<BandalartDatabase>().bandalartDao() }
}
