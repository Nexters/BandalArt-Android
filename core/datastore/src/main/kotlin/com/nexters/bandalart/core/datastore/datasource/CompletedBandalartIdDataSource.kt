package com.nexters.bandalart.core.datastore.datasource

interface CompletedBandalartIdDataSource {
    suspend fun getPrevBandalartList(): List<Pair<Long, Boolean>>
    suspend fun upsertBandalartId(bandalartId: Long, isCompleted: Boolean)
    suspend fun checkCompletedBandalartId(bandalartId: Long): Boolean
    suspend fun deleteBandalartId(bandalartId: Long)
}
