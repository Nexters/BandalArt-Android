package com.nexters.bandalart.di

import com.nexters.bandalart.core.database.BandalartDatabaseFactory
import com.nexters.bandalart.core.datastore.BandalartDataStoreFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

actual val platformModule = module {
    single { BandalartDatabaseFactory(androidApplication()) }
    single { BandalartDataStoreFactory(androidApplication()) }
}
