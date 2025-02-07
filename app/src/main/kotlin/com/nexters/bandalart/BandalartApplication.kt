package com.nexters.bandalart

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.nexters.bandalart.core.database.BandalartDao
import com.nexters.bandalart.core.datastore.di.Bandalart
import com.nexters.bandalart.core.datastore.di.InAppUpdate
import com.slack.circuit.foundation.Circuit
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

class BandalartApplication : Application() {
    lateinit var component: BandalartComponent

    override fun onCreate() {
        super.onCreate()
        component = BandalartComponent::class.create()
    }
}

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
interface BandalartComponent {
//    val bandalartDao: BandalartDao
//
//    @Bandalart
//    val bandalartDataStore: DataStore<Preferences>
//
//    @InAppUpdate
//    val inAppUpdateDataStore: DataStore<Preferences>

    fun circuit(): Circuit

    companion object {
        fun create(context: Context) = BandalartComponent::class.create()
    }
}

//@ContributesTo(AppScope::class)
//interface ApplicationComponent {
//    @Provides
//    @SingleIn(AppScope::class)
//    fun provideContext(): Context
//}
