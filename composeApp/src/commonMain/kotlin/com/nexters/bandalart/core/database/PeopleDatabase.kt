package com.nexters.bandalart.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(
    entities = [Person::class],
    version = 1,
)
@ConstructedBy(PeopleDatabaseConstructor::class)
abstract class PeopleDatabase : RoomDatabase() {
    abstract fun peopleDao(): PeopleDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object PeopleDatabaseConstructor : RoomDatabaseConstructor<PeopleDatabase> {
    override fun initialize(): PeopleDatabase
}
