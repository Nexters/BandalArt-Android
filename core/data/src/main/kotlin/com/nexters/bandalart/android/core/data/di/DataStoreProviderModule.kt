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

// DataStore 의 네이밍을 변경하면 데이터가 바뀌므로 임시로 주석 처리
// private const val BANDALART_DATASTORE = "bandalart_datastore"
// private val Context.bandalartDataStore: DataStore<Preferences> by preferencesDataStore(name = BANDALART_DATASTORE)

private const val TOKEN_DATASTORE = "token_datastore"
private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = TOKEN_DATASTORE)

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreProviderModule {

//  @Provides
//  @Singleton
//  fun providePreferencesDataStore(@ApplicationContext context: Context) = context.bandalartDataStore

  @Provides
  @Singleton
  fun providePreferencesDataStore(@ApplicationContext context: Context) = context.tokenDataStore

  @Provides
  @Singleton
  fun provideDataStoreProvider(dataStore: DataStore<Preferences>): DataStoreProvider =
    DataStoreProvider(dataStore)
}
