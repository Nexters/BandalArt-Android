package com.nexters.bandalart.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.nexters.bandalart.core.database.entity.BandalartCellDBEntity
import com.nexters.bandalart.core.database.entity.BandalartDBEntity

@Database(
    entities = [
        BandalartDBEntity::class,
        BandalartCellDBEntity::class,
    ],
    version = 1,
)
@ConstructedBy(BandalartDatabaseConstructor::class)
abstract class BandalartDatabase : RoomDatabase() {
    abstract fun bandalartDao(): BandalartDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object BandalartDatabaseConstructor : RoomDatabaseConstructor<BandalartDatabase> {
    override fun initialize(): BandalartDatabase
}
