package com.nexters.bandalart.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Bandalart::class],
    version = 1
)
abstract class BandalartDatabase: RoomDatabase() {
    abstract fun bandalartDao(): BandalartDao
}
