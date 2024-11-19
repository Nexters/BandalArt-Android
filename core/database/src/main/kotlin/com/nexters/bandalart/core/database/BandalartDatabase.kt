package com.nexters.bandalart.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nexters.bandalart.core.database.entity.BandalartCellDBEntity
import com.nexters.bandalart.core.database.entity.BandalartDetailDBEntity
import com.nexters.bandalart.core.database.entity.BandalartDBEntity

@Database(
    entities = [
        BandalartDBEntity::class,
        BandalartDetailDBEntity::class,
        BandalartCellDBEntity::class,
    ],
    version = 1,
    exportSchema = true,
)

abstract class BandalartDatabase : RoomDatabase() {
    abstract fun bandalartDao(): BandalartDao
}
