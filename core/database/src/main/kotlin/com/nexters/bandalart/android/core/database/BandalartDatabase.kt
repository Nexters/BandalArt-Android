package com.nexters.bandalart.android.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nexters.bandalart.android.core.database.entity.BandalartCellEntity
import com.nexters.bandalart.android.core.database.entity.BandalartDetailEntity
import com.nexters.bandalart.android.core.database.entity.BandalartEntity

@Database(
    entities = [
        BandalartEntity::class,
        BandalartDetailEntity::class,
        BandalartCellEntity::class
    ],
    version = 1,
    exportSchema = true,
)

abstract class BandalartDatabase : RoomDatabase() {
    abstract fun bandalartDao(): BandalartDao
}
