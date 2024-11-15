package com.nexters.bandalart.android.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bandalart_details")
data class BandalartDetailEntity(
  @PrimaryKey
  @ColumnInfo(name = "id")
  val id: String,
  @ColumnInfo(name = "mainColor")
  val mainColor: String,
  @ColumnInfo(name = "subColor")
  val subColor: String,
  @ColumnInfo(name = "profileEmoji")
  val profileEmoji: String?,
  @ColumnInfo(name = "title")
  val title: String?,
  @ColumnInfo(name = "cellId")
  val cellId: String,
  @ColumnInfo(name = "dueDate")
  val dueDate: String?,
  @ColumnInfo(name = "isCompleted")
  val isCompleted: Boolean,
  @ColumnInfo(name = "completionRatio")
  val completionRatio: Int,
)
