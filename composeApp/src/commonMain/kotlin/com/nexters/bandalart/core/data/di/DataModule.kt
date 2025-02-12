package com.nexters.bandalart.core.data.di

import com.nexters.bandalart.core.database.di.daoModule
import com.nexters.bandalart.core.datastore.di.dataStoreModule
import com.nexters.bandalart.core.domain.repository.BandalartRepository
import com.nexters.bandalart.core.data.repository.BandalartRepositoryImpl
import com.nexters.bandalart.core.domain.repository.InAppUpdateRepository
import com.nexters.bandalart.core.data.repository.InAppUpdateRepositoryImpl
import com.nexters.bandalart.core.domain.repository.OnboardingRepository
import com.nexters.bandalart.core.data.repository.OnboardingRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule =
    module {
        includes(
            daoModule,
            dataStoreModule,
        )
        singleOf(::BandalartRepositoryImpl) bind BandalartRepository::class
        singleOf(::InAppUpdateRepositoryImpl) bind InAppUpdateRepository::class
        singleOf(::OnboardingRepositoryImpl) bind OnboardingRepository::class
    }
