package com.nexters.bandalart.android.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nexters.bandalart.android.core.datastore.DataStoreProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val BANDALART_DATASTORE = "bandalart_datastore"
private val Context.bandalartDataStore: DataStore<Preferences> by preferencesDataStore(name = BANDALART_DATASTORE)

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {
    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context) = context.bandalartDataStore

    @Provides
    fun provideDataStore(dataStore: DataStore<Preferences>) = DataStoreProvider(dataStore)
}
