package com.nexters.bandalart.android.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
  tableName = "bandalart_details",
  foreignKeys = [
    ForeignKey(
      entity = BandalartDBEntity::class,
      parentColumns = ["id"],
      childColumns = ["id"],
      onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
      entity = BandalartCellDBEntity::class,
      parentColumns = ["id"],
      childColumns = ["cellId"],
      onDelete = ForeignKey.CASCADE
    )
  ]
)
data class BandalartDetailDBEntity(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  val id: Long,

  @ColumnInfo(name = "mainColor")
  val mainColor: String = "0xFF3FFFBA",

  @ColumnInfo(name = "subColor")
  val subColor: String = "0xFF111827",

  @ColumnInfo(name = "profileEmoji")
  val profileEmoji: String? = null,

  @ColumnInfo(name = "title")
  val title: String? = null,

  @ColumnInfo(name = "cellId")
  val cellId: Long,

  @ColumnInfo(name = "dueDate")
  val dueDate: String? = null,

  @ColumnInfo(name = "isCompleted")
  val isCompleted: Boolean = false,

  @ColumnInfo(name = "completionRatio")
  val completionRatio: Int = 0,
)
