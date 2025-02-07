package com.nexters.bandalart.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BandalartDao {
    @Upsert
    suspend fun upsert(bandalart: Bandalart)

    @Delete
    suspend fun delete(bandalart: Bandalart)

    @Query("SELECT * FROM person")
    fun getAllBandalart(): Flow<List<Bandalart>>
}
