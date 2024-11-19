package com.nexters.bandalart.android.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bandalarts")
data class BandalartDBEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null,

    @ColumnInfo(name = "mainColor")
    val mainColor: String = "0xFF3FFFBA",

    @ColumnInfo(name = "subColor")
    val subColor: String = "0xFF111827",

    @ColumnInfo(name = "profileEmoji")
    val profileEmoji: String? = null,

    @ColumnInfo(name = "completionRatio")
    val completionRatio: Int = 0,
)
