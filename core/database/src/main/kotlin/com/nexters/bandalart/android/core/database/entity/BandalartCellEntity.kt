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
      entity = BandalartEntity::class,
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
data class BandalartCellEntity(
  @PrimaryKey
  @ColumnInfo(name = "id")
  val id: String,
  @ColumnInfo(name = "bandalartId")
  val bandalartId: String,
  @ColumnInfo(name = "title")
  val title: String?,
  @ColumnInfo(name = "description")
  val description: String?,
  @ColumnInfo(name = "dueDate")
  val dueDate: String?,
  @ColumnInfo(name = "isCompleted")
  val isCompleted: Boolean,
  @ColumnInfo(name = "completionRatio")
  val completionRatio: Int,
  @ColumnInfo(name = "profileEmoji")
  val profileEmoji: String?,
  @ColumnInfo(name = "mainColor")
  val mainColor: String?,
  @ColumnInfo(name = "subColor")
  val subColor: String?,
  @ColumnInfo(name = "parentId")
  val parentId: String?
)
