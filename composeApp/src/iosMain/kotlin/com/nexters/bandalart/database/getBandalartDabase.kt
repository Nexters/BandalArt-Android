package com.nexters.bandalart.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import platform.Foundation.NSHomeDirectory

//fun getBandalartDatabase(): BandalartDatabase {
//    val dbFile = NSHomeDirectory() + "/bandalart.db"
//    return Room.databaseBuilder<BandalartDatabase>(
//        name = dbFile,
//        factory = { BandalartDatabase::class.instantiateImpl() }
//    )
//        .setDriver(BundledSQLiteDriver())
//        .build()
//}
