package com.nexters.bandalart.di

import com.nexters.bandalart.core.data.di.dataModule
import com.nexters.bandalart.core.database.di.databaseModule
import com.nexters.bandalart.core.datastore.di.dataStoreModule
import com.nexters.bandalart.feature.complete.di.completeModule
import com.nexters.bandalart.feature.home.di.homeModule
import com.nexters.bandalart.feature.onboarding.di.onboardingModule
import com.nexters.bandalart.feature.splash.di.splashModule
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val coreModule = module {
    includes(
        dataModule,
        databaseModule,
        dataStoreModule,
    )
}

val featureModule = module {
    includes(
        completeModule,
        homeModule,
        onboardingModule,
        splashModule,
    )
}

val appModule = module {
    includes(
        coreModule,
        featureModule,
    )
}
