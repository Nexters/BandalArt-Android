package com.nexters.bandalart.android.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.nexters.bandalart.android.core.data.local.DataStoreProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATASTORE_PREFERENCES = "datastore_preferences"

@Module
@InstallIn(SingletonComponent::class)
object DataStoreProviderModule {

  @Provides
  @Singleton
  fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
    PreferenceDataStoreFactory.create(produceFile = { context.preferencesDataStoreFile(DATASTORE_PREFERENCES) })

  @Provides
  @Singleton
  fun provideDataStoreProvider(dataStore: DataStore<Preferences>): DataStoreProvider =
    DataStoreProvider(dataStore)
}
