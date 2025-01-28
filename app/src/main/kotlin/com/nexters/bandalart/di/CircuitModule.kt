package com.nexters.bandalart.di

import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

//@Module
//@InstallIn(ActivityRetainedComponent::class)
//abstract class CircuitModule {
//
//    @Multibinds
//    abstract fun presenterFactories(): Set<Presenter.Factory>
//
//    @Multibinds
//    abstract fun uiFactories(): Set<Ui.Factory>
//
//    companion object {
//        @[Provides ActivityRetainedScoped]
//        fun provideCircuit(
//            presenterFactories: @JvmSuppressWildcards Set<Presenter.Factory>,
//            uiFactories: @JvmSuppressWildcards Set<Ui.Factory>,
//        ): Circuit = Circuit.Builder()
//            .addPresenterFactories(presenterFactories)
//            .addUiFactories(uiFactories)
//            .build()
//    }
//}

@ContributesTo(AppScope::class)
interface CircuitComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideCircuit(
        uiFactories: Set<Ui.Factory>,
        presenterFactories: Set<Presenter.Factory>,
    ): Circuit = Circuit.Builder()
        .addUiFactories(uiFactories)
        .addPresenterFactories(presenterFactories)
        .build()
}
