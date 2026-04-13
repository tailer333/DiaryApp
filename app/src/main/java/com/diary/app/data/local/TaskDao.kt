package com.diary.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query(
        """
        SELECT * FROM tasks
        WHERE dateFinishMillis > :dayStartMillis AND dateStartMillis < :dayEndMillis
        ORDER BY dateStartMillis ASC
        """,
    )
    fun observeTasksForDay(dayStartMillis: Long, dayEndMillis: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: Long): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TaskEntity): Long

    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun count(): Int
}
