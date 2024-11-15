package com.nexters.bandalart.android.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
  tableName = "bandalart_cells",
  foreignKeys = [
    ForeignKey(
      entity = BandalartDBEntity::class,
      parentColumns = ["id"],
      childColumns = ["bandalartId"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [
    Index(value = ["parentId"]),
    Index(value = ["bandalartId"])
  ]
)
data class BandalartCellDBEntity(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  val id: Long,

  @ColumnInfo(name = "bandalartId")
  val bandalartId: String,

  @ColumnInfo(name = "title")
  val title: String? = null,

  @ColumnInfo(name = "description")
  val description: String? = null,

  @ColumnInfo(name = "dueDate")
  val dueDate: String? = null,

  @ColumnInfo(name = "isCompleted")
  val isCompleted: Boolean = false,

  @ColumnInfo(name = "completionRatio")
  val completionRatio: Int = 0,

  @ColumnInfo(name = "profileEmoji")
  val profileEmoji: String? = null,

  @ColumnInfo(name = "mainColor")
  val mainColor: String? = "0xFF3FFFBA",

  @ColumnInfo(name = "subColor")
  val subColor: String? = "0xFF111827",

  @ColumnInfo(name = "parentId")
  val parentId: Long? = null
)
