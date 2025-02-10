package com.nexters.bandalart.di

import com.nexters.bandalart.core.data.di.dataModule
import com.nexters.bandalart.feature.complete.di.completeModule
import com.nexters.bandalart.feature.home.di.homeModule
import com.nexters.bandalart.feature.onboarding.di.onboardingModule
import com.nexters.bandalart.feature.splash.di.splashModule
import com.nexters.bandalart.feature.complete.viewmodel.CompleteViewModel
import com.nexters.bandalart.feature.home.viewmodel.HomeViewModel
import com.nexters.bandalart.feature.onboarding.OnboardingViewModel
import com.nexters.bandalart.feature.splash.SplashViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf

val featureModule =
    module {
        includes(
            completeModule,
            homeModule,
            onboardingModule,
            splashModule,
        )
    }

val appModule =
    module {
        includes(
            dataModule,
            featureModule,
        )
        viewModelOf(::CompleteViewModel)
        viewModelOf(::HomeViewModel)
        viewModelOf(::OnboardingViewModel)
        viewModelOf(::SplashViewModel)
    }
