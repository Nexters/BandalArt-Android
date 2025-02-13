package com.nexters.bandalart.core.database.di

import com.nexters.bandalart.core.database.BandalartDatabase
import org.koin.dsl.module

val daoModule = module {
    includes(databaseModule)

    single { get<BandalartDatabase>().bandalartDao() }
}
