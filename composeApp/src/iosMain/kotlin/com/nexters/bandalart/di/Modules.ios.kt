package com.nexters.bandalart.di

import com.nexters.bandalart.core.common.AppVersionProvider
import com.nexters.bandalart.core.common.ImageHandlerProvider
import com.nexters.bandalart.core.database.BandalartDatabaseFactory
import com.nexters.bandalart.core.datastore.BandalartDataStoreFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { BandalartDatabaseFactory() }
        single { BandalartDataStoreFactory() }
        single { AppVersionProvider() }
        single { ImageHandlerProvider() }
    }
