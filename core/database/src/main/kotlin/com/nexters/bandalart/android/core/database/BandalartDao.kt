package com.nexters.bandalart.android.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.nexters.bandalart.android.core.database.entity.BandalartCellEntity
import com.nexters.bandalart.android.core.database.entity.BandalartCellWithChildrenDto
import com.nexters.bandalart.android.core.database.entity.BandalartDetailEntity
import com.nexters.bandalart.android.core.database.entity.BandalartEntity
import com.nexters.bandalart.android.core.database.entity.UpdateBandalartEmojiDto
import com.nexters.bandalart.android.core.database.entity.UpdateBandalartMainCellDto
import com.nexters.bandalart.android.core.database.entity.UpdateBandalartSubCellDto
import com.nexters.bandalart.android.core.database.entity.UpdateBandalartTaskCellDto

@Dao
interface BandalartDao {
  // ===== 생성 관련 함수들 =====
  /** 새로운 반다라트 생성 */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun createBandalart(bandalart: BandalartEntity): Long

  /** 반다라트 상세 정보 삽입 */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBandalartDetail(detail: BandalartDetailEntity)

  /** 반다라트 셀 삽입 */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCell(cell: BandalartCellEntity)

  /** 부모 셀과 자식 셀들을 한번에 삽입 */
  @Transaction
  suspend fun insertCellWithChildren(cell: BandalartCellEntity, children: List<BandalartCellEntity>) {
    insertCell(cell)
    children.forEach { childCell ->
      insertCell(childCell.copy(parentId = cell.id))
    }
  }

  // ===== 조회 관련 함수들 - 반다라트 =====
  /** 모든 반다라트 목록 조회 */
  @Query("SELECT * FROM bandalarts")
  suspend fun getBandalartList(): List<BandalartEntity>

  /** 특정 반다라트의 상세 정보 조회 */
  @Query("SELECT * FROM bandalart_details WHERE id = :bandalartId")
  suspend fun getBandalartDetail(bandalartId: String): BandalartDetailEntity

  // ===== 조회 관련 함수들 - 셀 =====
  /** 특정 반다라트의 메인 셀(최상위 셀) 조회 */
  @Transaction
  @Query("SELECT * FROM bandalart_cells WHERE bandalartId = :bandalartId AND parentId IS NULL")
  suspend fun getBandalartMainCell(bandalartId: String): BandalartCellWithChildrenDto

  /** 특정 셀과 그 자식 셀들 조회 */
  @Transaction
  @Query("SELECT * FROM bandalart_cells WHERE id = :cellId")
  suspend fun getBandalartCell(cellId: String): BandalartCellWithChildrenDto

  /** 특정 셀의 자식 셀들 조회 */
  @Query("SELECT * FROM bandalart_cells WHERE parentId = :parentId")
  suspend fun getChildCells(parentId: String): List<BandalartCellEntity>

  // ===== 업데이트 관련 함수들 - 반다라트 =====
  /** 반다라트 정보 업데이트 */
  @Update
  suspend fun updateBandalart(bandalart: BandalartEntity)

  /** 반다라트 상세 정보 업데이트 */
  @Update
  suspend fun updateBandalartDetail(detail: BandalartDetailEntity)

  // ===== 업데이트 관련 함수들 - 셀 =====
  /** 메인 셀 정보 업데이트 */
  @Transaction
  suspend fun updateMainCellWithDto(
    cellKey: String,
    updateDto: UpdateBandalartMainCellDto,
  ) {
    val cell = getBandalartCell(cellKey).cell.copy(
      title = updateDto.title,
      description = updateDto.description,
      dueDate = updateDto.dueDate,
      profileEmoji = updateDto.profileEmoji,
      mainColor = updateDto.mainColor,
      subColor = updateDto.subColor,
    )
    updateCell(cell)
  }

  /** 서브 셀 정보 업데이트 */
  @Transaction
  suspend fun updateSubCellWithDto(
    cellKey: String,
    updateDto: UpdateBandalartSubCellDto,
  ) {
    val cell = getBandalartCell(cellKey).cell.copy(
      title = updateDto.title,
      description = updateDto.description,
      dueDate = updateDto.dueDate,
    )
    updateCell(cell)
  }

  /** 작업 셀 정보 업데이트 */
  @Transaction
  suspend fun updateTaskCellWithDto(
    cellKey: String,
    updateDto: UpdateBandalartTaskCellDto,
  ) {
    val originalCell = getBandalartCell(cellKey).cell
    val updatedCell = originalCell.copy(
      title = updateDto.title,
      description = updateDto.description,
      dueDate = updateDto.dueDate,
      isCompleted = updateDto.isCompleted ?: originalCell.isCompleted
    )
    updateCell(updatedCell)
  }

  /** 셀의 이모지 업데이트 */
  @Transaction
  suspend fun updateEmojiWithDto(
    cellId: String,
    updateDto: UpdateBandalartEmojiDto
  ) {
    val originalCell = getBandalartCell(cellId).cell
    val updatedCell = originalCell.copy(
      profileEmoji = updateDto.profileEmoji
    )
    updateCell(updatedCell)
  }

  /** 셀 정보 업데이트 */
  @Update
  suspend fun updateCell(cell: BandalartCellEntity)

  // ===== 삭제 관련 함수들 =====
  /** 반다라트 삭제 */
  @Delete
  suspend fun deleteBandalart(bandalart: BandalartEntity)

  /** 특정 셀 삭제 */
  @Query("DELETE FROM bandalart_cells WHERE id = :cellId")
  suspend fun deleteBandalartCell(cellId: String)

  // ===== 완료율 관련 함수들 =====
  /** 특정 반다라트의 모든 셀 완료율 조회 */
  @Query("SELECT completionRatio FROM bandalart_cells WHERE bandalartId = :bandalartId")
  suspend fun getCellCompletionRatios(bandalartId: String): List<Int>

  /** 반다라트의 완료율 업데이트 */
  @Query("UPDATE bandalarts SET completionRatio = :newRatio WHERE id = :bandalartId")
  suspend fun updateRatio(bandalartId: String, newRatio: Int)

  /** 반다라트의 전체 완료율 계산 및 업데이트 */
  @Transaction
  suspend fun updateBandalartCompletionRatio(bandalartId: String) {
    val ratios = getCellCompletionRatios(bandalartId)
    val newRatio = if (ratios.isEmpty()) 0 else ratios.average().toInt()
    updateRatio(bandalartId, newRatio)
  }

  /** 셀의 완료율 업데이트 */
  @Query("UPDATE bandalart_cells SET completionRatio = :newRatio WHERE id = :cellId")
  suspend fun updateCellRatio(cellId: String, newRatio: Int)

  /** 셀의 완료율 계산 및 업데이트 (자식 셀들의 평균) */
  @Transaction
  suspend fun updateCellCompletionRatio(cellId: String) {
    val cell = getBandalartCell(cellId)
    val childRatios = getChildCells(cellId).map { it.completionRatio }
    val newRatio = if (childRatios.isEmpty()) {
      if (cell.cell.isCompleted) 100 else 0
    } else {
      childRatios.average().toInt()
    }
    updateCellRatio(cellId, newRatio)
  }

  // ===== 유틸리티 함수들 =====
  /** 특정 셀의 존재 여부 확인 */
  @Query("SELECT EXISTS(SELECT 1 FROM bandalart_cells WHERE id = :cellId)")
  suspend fun cellExists(cellId: String): Boolean

  /** 특정 반다라트의 존재 여부 확인 */
  @Query("SELECT EXISTS(SELECT 1 FROM bandalarts WHERE id = :bandalartId)")
  suspend fun bandalartExists(bandalartId: String): Boolean
}

