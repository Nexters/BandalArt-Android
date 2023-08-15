package com.nexters.bandalart.android.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nexters.bandalart.android.core.data.local.DataStoreProvider
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
internal object DataStoreProviderModule {

  @Provides
  @Singleton
  fun providePreferencesDataStore(@ApplicationContext context: Context) = context.bandalartDataStore

  @Provides
  @Singleton
  fun provideDataStoreProvider(dataStore: DataStore<Preferences>): DataStoreProvider =
    DataStoreProvider(dataStore)
}
