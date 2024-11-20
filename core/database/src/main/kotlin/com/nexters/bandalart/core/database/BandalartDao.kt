package com.nexters.bandalart.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.nexters.bandalart.core.database.entity.BandalartCellDBEntity
import com.nexters.bandalart.core.database.entity.BandalartCellWithChildrenDto
import com.nexters.bandalart.core.database.entity.BandalartDBEntity
import com.nexters.bandalart.core.database.entity.UpdateBandalartEmojiDto
import com.nexters.bandalart.core.database.entity.UpdateBandalartMainCellDto
import com.nexters.bandalart.core.database.entity.UpdateBandalartSubCellDto
import com.nexters.bandalart.core.database.entity.UpdateBandalartTaskCellDto

@Dao
interface BandalartDao {
    // Create
    /** 새로운 반다라트 생성 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createBandalart(bandalart: BandalartDBEntity): Long

    /** 반다라트 셀 삽입 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCell(cell: BandalartCellDBEntity): Long

    /** 부모 셀과 자식 셀들을 한번에 삽입 */
    @Transaction
    suspend fun insertCellWithChildren(cell: BandalartCellDBEntity, children: List<BandalartCellDBEntity>) {
        val parentId = insertCell(cell)
        children.forEach { childCell ->
            insertCell(childCell.copy(parentId = parentId))
        }
    }

    /** 빈 반다라트 생성 (메인 셀 1개, 서브 셀 4개, 각 서브 셀당 하위 셀 5개) */
    @Transaction
    suspend fun createEmptyBandalart(): Long {
        // 1. 기본 반다라트 생성
        val bandalartId = createBandalart(
            BandalartDBEntity(
                mainColor = "#3FFFBA",
                subColor = "#111827",
            ),
        )

        // 2. 메인 셀 생성
        val mainCellId = insertCell(
            BandalartCellDBEntity(
                bandalartId = bandalartId,
                title = "",
            ),
        )

        // 3. 4개의 서브 셀 생성
        repeat(4) {
            val subCellId = insertCell(
                BandalartCellDBEntity(
                    bandalartId = bandalartId,
                    parentId = mainCellId,
                ),
            )

            // 4. 각 서브 셀마다 5개의 태스크 셀 생성
            repeat(5) {
                insertCell(
                    BandalartCellDBEntity(
                        bandalartId = bandalartId,
                        parentId = subCellId,
                    ),
                )
            }
        }

        return bandalartId
    }

    // Read - 반다라트
    @Query("SELECT * FROM bandalarts WHERE id = :bandalartId")
    suspend fun getBandalart(bandalartId: Long): BandalartDBEntity

    /** 모든 반다라트 목록 조회 */
    @Query("SELECT * FROM bandalarts")
    suspend fun getBandalartList(): List<BandalartDBEntity>

    // Read - 셀
    /** 특정 반다라트의 메인 셀(최상위 셀) 조회 */
    @Transaction
    @Query("SELECT * FROM bandalart_cells WHERE bandalartId = :bandalartId AND parentId IS NULL")
    suspend fun getBandalartMainCell(bandalartId: Long): BandalartCellWithChildrenDto

    /** 특정 셀 조회 */
    @Query("SELECT * FROM bandalart_cells WHERE id = :cellId")
    suspend fun getCell(cellId: Long): BandalartCellDBEntity

    /** 특정 셀과 그 자식 셀들 조회 */
    @Transaction
    @Query("SELECT * FROM bandalart_cells WHERE id = :cellId")
    suspend fun getBandalartCell(cellId: Long): BandalartCellWithChildrenDto

    /** 특정 셀의 자식 셀들 조회 */
    @Query("SELECT * FROM bandalart_cells WHERE parentId = :parentId")
    suspend fun getChildCells(parentId: Long): List<BandalartCellDBEntity>

    @Query("SELECT * FROM bandalart_cells WHERE bandalartId = :bandalartId")
    suspend fun getAllCellsInBandalart(bandalartId: Long): List<BandalartCellDBEntity>

    // Update - 반다라트
    /** 반다라트 정보 업데이트 */
    @Update
    suspend fun updateBandalart(bandalart: BandalartDBEntity)

    // Update - 셀
    /** 메인 셀 정보 업데이트 */
    @Transaction
    suspend fun updateMainCellWithDto(
        cellId: Long,
        updateDto: UpdateBandalartMainCellDto,
    ) {
        val bandalartCell = getBandalartCell(cellId)
        val currentBandalart = getBandalart(bandalartCell.cell.bandalartId)

        updateBandalart(
            currentBandalart.copy(
                title = updateDto.title,
                description = updateDto.description,
                dueDate = updateDto.dueDate,
                profileEmoji = updateDto.profileEmoji,
                mainColor = updateDto.mainColor,
                subColor = updateDto.subColor,
            )
        )

        updateCell(bandalartCell.cell.copy(
            title = updateDto.title,
            description = updateDto.description,
            dueDate = updateDto.dueDate
        ))
    }

    /** 서브 셀 정보 업데이트 */
    @Transaction
    suspend fun updateSubCellWithDto(
        cellId: Long,
        updateDto: UpdateBandalartSubCellDto,
    ) {
        val cell = getBandalartCell(cellId).cell.copy(
            title = updateDto.title,
            description = updateDto.description,
            dueDate = updateDto.dueDate,
        )
        updateCell(cell)
        updateCompletionStatus(cell.bandalartId)
    }

    /** 태스크 셀 정보 업데이트 */
    @Transaction
    suspend fun updateTaskCellWithDto(
        cellId: Long,
        updateDto: UpdateBandalartTaskCellDto,
    ) {
        val originalCell = getBandalartCell(cellId).cell
        val updatedCell = originalCell.copy(
            title = updateDto.title,
            description = updateDto.description,
            dueDate = updateDto.dueDate,
            isCompleted = updateDto.isCompleted ?: originalCell.isCompleted,
        )
        updateCell(updatedCell)

        // 태스크 셀이 업데이트되면 전체 완료 상태 업데이트
        updateCompletionStatus(updatedCell.bandalartId)
    }

    /** 셀의 이모지 업데이트 */
    @Transaction
    suspend fun updateEmojiWithDto(
        cellId: Long,
        updateDto: UpdateBandalartEmojiDto,
    ) {
        val bandalartCell = getBandalartCell(cellId)
        updateBandalart(
            getBandalart(bandalartCell.cell.bandalartId).copy(
                profileEmoji = updateDto.profileEmoji
            )
        )
    }

    /** 셀 정보 업데이트 */
    @Update
    suspend fun updateCell(cell: BandalartCellDBEntity)

    @Query("UPDATE bandalarts SET completionRatio = :ratio WHERE id = :bandalartId")
    suspend fun updateBandalartRatio(bandalartId: Long, ratio: Int)

    // 삭제 관련 함수
    /** 반다라트 삭제 */
    @Delete
    suspend fun deleteBandalart(bandalart: BandalartDBEntity)

    /** 특정 셀 삭제 */
    @Transaction
    suspend fun deleteCellOrReset(cellId: Long) {
        val cell = getCell(cellId)
        when {
            // MainCell (부모가 없는 경우)
            cell.parentId == null -> {
                // MainCell 의 경우 해당 반다라트 자체를 삭제
                deleteBandalart(getBandalart(cell.bandalartId))
            }

            // TaskCell (부모의 부모가 있는 경우)
            getCell(cell.parentId).parentId != null -> resetTaskCell(cellId)

            // SubCell (부모가 있고, 그 부모가 MainCell인 경우)
            else -> resetSubCellWithChildren(cellId)
        }
    }

    @Transaction
    suspend fun resetSubCellWithChildren(cellId: Long) {
        val subCell = getCell(cellId)
        // SubCell 초기화
        updateCell(
            subCell.copy(
                title = "",
                description = null,
                dueDate = null,
                isCompleted = false,
            ),
        )

        // 자식 TaskCell 들도 초기화
        val taskCells = getChildCells(cellId)
        taskCells.forEach { taskCell ->
            updateCell(
                taskCell.copy(
                    title = "",
                    description = null,
                    dueDate = null,
                    isCompleted = false,
                ),
            )
        }
        updateCompletionStatus(subCell.bandalartId)
    }

    @Transaction
    suspend fun resetTaskCell(cellId: Long) {
        val taskCell = getCell(cellId)
        updateCell(
            taskCell.copy(
                title = "",
                description = null,
                dueDate = null,
                isCompleted = false,
            ),
        )
        updateCompletionStatus(taskCell.bandalartId)
    }

    /** 반다라트의 완료율 업데이트 */
    @Query("UPDATE bandalarts SET completionRatio = :newRatio WHERE id = :bandalartId")
    suspend fun updateRatio(bandalartId: Long, newRatio: Int)

    /** 반다라트의 전체 완료율 계산 및 업데이트 */
    private suspend fun updateCompletionStatus(bandalartId: Long) {
        val allCells = getAllCellsInBandalart(bandalartId)
        val completedCells = allCells.count { it.isCompleted }
        val totalCompletionRatio = completedCells * 4  // 각 셀당 4%

        // 반다라트 전체 완료율 업데이트
        updateBandalartRatio(bandalartId, totalCompletionRatio)

        // 각 서브 목표의 자동 완료 체크
        val mainCell = getBandalartMainCell(bandalartId).cell
        val subGoals = mainCell.id?.let { getChildCells(it) }

        subGoals?.forEach { subGoal ->
            val subGoalChildren = subGoal.id?.let { getChildCells(it) }
            val allChildrenCompleted = subGoalChildren?.all { it.isCompleted }
            if (allChildrenCompleted == true && !subGoal.isCompleted) {
                updateCell(subGoal.copy(isCompleted = true))
            }
        }

        // 메인 목표 자동 완료 체크
        val allSubGoalsCompleted = subGoals?.all { it.isCompleted }
        if (allSubGoalsCompleted == true && !mainCell.isCompleted) {
            updateCell(mainCell.copy(isCompleted = true))
        }
    }

    // Util function
    /** 특정 셀의 존재 여부 확인 */
    @Query("SELECT EXISTS(SELECT 1 FROM bandalart_cells WHERE id = :cellId)")
    suspend fun cellExists(cellId: String): Boolean

    /** 특정 반다라트의 존재 여부 확인 */
    @Query("SELECT EXISTS(SELECT 1 FROM bandalarts WHERE id = :bandalartId)")
    suspend fun bandalartExists(bandalartId: String): Boolean
}

