package com.nexters.bandalart.android.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bandalarts")
data class BandalartEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "mainColor")
    val mainColor: String,
    @ColumnInfo(name = "subColor")
    val subColor: String,
    @ColumnInfo(name = "profileEmoji")
    val profileEmoji: String?,
    @ColumnInfo(name = "completionRatio")
    val completionRatio: Int,
)
