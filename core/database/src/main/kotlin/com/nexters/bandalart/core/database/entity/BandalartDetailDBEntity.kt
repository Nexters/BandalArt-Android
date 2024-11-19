package com.nexters.bandalart.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * 반다라트의 메인 목표(root)에 대한 상세 정보를 저장하는 테이블 엔티티
 * 각 반다라트의 대표 정보, 진행 상태 등을 관리
 */
@Entity(
    tableName = "bandalart_details",
    foreignKeys = [
        // 메인 반다라트 테이블과의 관계 설정
        ForeignKey(
            // 참조할 반다라트 메인 엔티티
            entity = BandalartDBEntity::class,
            // 반다라트의 ID
            parentColumns = ["id"],
            // 현재 테이블의 ID (1:1 관계)
            childColumns = ["id"],
            // 반다라트 삭제 시 상세 정보도 함께 삭제
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class BandalartDetailDBEntity(
    // 기본 키 - 반다라트의 ID와 1:1 매칭
    // 연관된 반다라트의 ID와 동일한 값 사용
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "mainColor")
    val mainColor: String = "#FF3FFFBA",

    @ColumnInfo(name = "subColor")
    val subColor: String = "#FF111827",

    @ColumnInfo(name = "profileEmoji")
    val profileEmoji: String? = null,

    @ColumnInfo(name = "title")
    val title: String? = null,

    @ColumnInfo(name = "dueDate")
    val dueDate: String? = null,

    @ColumnInfo(name = "isCompleted")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "completionRatio")
    val completionRatio: Int = 0,
)
