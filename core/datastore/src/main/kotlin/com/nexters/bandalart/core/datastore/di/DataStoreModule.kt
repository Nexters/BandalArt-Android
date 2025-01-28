package com.nexters.bandalart.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nexters.bandalart.core.datastore.BandalartDataStore
import com.nexters.bandalart.core.datastore.InAppUpdateDataStore
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

//private const val BANDALART_DATASTORE = "bandalart_datastore"
//private const val IN_APP_UPDATE_DATASTORE = "in_app_update_datastore"
//private val Context.bandalartDataStore: DataStore<Preferences> by preferencesDataStore(name = BANDALART_DATASTORE)
//private val Context.inAppUpdateDataStore: DataStore<Preferences> by preferencesDataStore(name = IN_APP_UPDATE_DATASTORE)
//
//@Module
//@InstallIn(SingletonComponent::class)
//internal object DataStoreModule {
//    @Provides
//    @Singleton
//    @Bandalart
//    fun provideBandalartPreferencesDataStore(@ApplicationContext context: Context) = context.bandalartDataStore
//
//    @Provides
//    @Singleton
//    @InAppUpdate
//    fun provideInAppUpdatePreferencesDataStore(@ApplicationContext context: Context) = context.inAppUpdateDataStore
//
//    @Provides
//    @Singleton
//    fun provideBandalartDataStore(@Bandalart dataStore: DataStore<Preferences>) = BandalartDataStore(dataStore)
//
//    @Provides
//    @Singleton
//    fun provideInAppUpdateDataStore(@InAppUpdate dataStore: DataStore<Preferences>) = InAppUpdateDataStore(dataStore)
//}

private const val BANDALART_DATASTORE = "bandalart_datastore"
private const val IN_APP_UPDATE_DATASTORE = "in_app_update_datastore"

private val Context.bandalartDataStore: DataStore<Preferences> by preferencesDataStore(name = BANDALART_DATASTORE)
private val Context.inAppUpdateDataStore: DataStore<Preferences> by preferencesDataStore(name = IN_APP_UPDATE_DATASTORE)

@ContributesTo(AppScope::class)
interface DataStoreComponent {
    @Provides
    @SingleIn(AppScope::class)
    @Bandalart
    fun provideBandalartDataStore(context: Context): DataStore<Preferences> = context.bandalartDataStore

    @Provides
    @SingleIn(AppScope::class)
    @InAppUpdate
    fun provideInAppUpdateDataStore(context: Context): DataStore<Preferences> = context.inAppUpdateDataStore

    @Provides
    @SingleIn(AppScope::class)
    fun provideBandalartDataStore(@Bandalart dataStore: DataStore<Preferences>): BandalartDataStore = BandalartDataStore(dataStore)

    @Provides
    @SingleIn(AppScope::class)
    fun provideInAppUpdateDataStore(@InAppUpdate dataStore: DataStore<Preferences>): InAppUpdateDataStore = InAppUpdateDataStore(dataStore)
}
