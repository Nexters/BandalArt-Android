package com.nexters.bandalart.core.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.nexters.bandalart.core.database.BandalartDatabase

fun createBandalartDatabase(ctx: Context): BandalartDatabase {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("bandalart.db")
    return Room.databaseBuilder<BandalartDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}
