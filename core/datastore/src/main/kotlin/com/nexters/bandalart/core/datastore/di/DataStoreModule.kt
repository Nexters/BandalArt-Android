package com.nexters.bandalart.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nexters.bandalart.core.datastore.BandalartDataStore
import com.nexters.bandalart.core.datastore.InAppUpdateDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val BANDALART_DATASTORE = "bandalart_datastore"
private const val IN_APP_UPDATE_DATASTORE = "in_app_update_datastore"
private val Context.bandalartDataStore: DataStore<Preferences> by preferencesDataStore(name = BANDALART_DATASTORE)
private val Context.inAppUpdateDataStore: DataStore<Preferences> by preferencesDataStore(name = IN_APP_UPDATE_DATASTORE)

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {
    @Provides
    @Singleton
    @Bandalart
    fun provideBandalartPreferencesDataStore(@ApplicationContext context: Context) = context.bandalartDataStore

    @Provides
    @Singleton
    @InAppUpdate
    fun provideInAppUpdatePreferencesDataStore(@ApplicationContext context: Context) = context.inAppUpdateDataStore

    @Provides
    @Singleton
    fun provideBandalartDataStore(@Bandalart dataStore: DataStore<Preferences>) = BandalartDataStore(dataStore)

    @Provides
    @Singleton
    fun provideInAppUpdateDataStore(@InAppUpdate dataStore: DataStore<Preferences>) = InAppUpdateDataStore(dataStore)
}
