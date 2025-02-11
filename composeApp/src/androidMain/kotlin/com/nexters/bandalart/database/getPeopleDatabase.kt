package com.nexters.bandalart.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

fun getDatabaseBuilder(ctx: Context): PeopleDatabase {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("people.db")
    return Room.databaseBuilder<PeopleDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}
