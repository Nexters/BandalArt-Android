package com.nexters.bandalart.core.common.di

import android.content.Context
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface ContextComponent {
    @Provides
    @SingleIn(AppScope::class)
    fun provideContext(): Context
}
