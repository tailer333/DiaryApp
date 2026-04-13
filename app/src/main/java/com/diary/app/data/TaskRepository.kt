package com.diary.app.data

import com.diary.app.data.local.TaskEntity
import com.diary.app.data.local.TaskDao
import com.diary.app.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId

class TaskRepository(
    private val taskDao: TaskDao,
    private val zoneId: ZoneId = ZoneId.systemDefault(),
) {

    fun observeTasksForDay(day: LocalDate): Flow<List<Task>> {
        val start = day.atStartOfDay(zoneId).toInstant().toEpochMilli()
        val end = day.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli()
        return taskDao.observeTasksForDay(start, end).map { list -> list.map { it.toDomain() } }
    }

    suspend fun getById(id: Long): Task? = taskDao.getById(id)?.toDomain()

    suspend fun insert(task: Task): Long {
        val entity = TaskEntity(
            id = if (task.id == 0L) 0 else task.id,
            dateStartMillis = task.dateStartMillis,
            dateFinishMillis = task.dateFinishMillis,
            name = task.name,
            description = task.description,
        )
        return taskDao.insert(entity)
    }

    suspend fun seedSampleTasksIfEmpty() {
        if (taskDao.count() > 0) return
        val today = LocalDate.now(zoneId)
        val start = today.atStartOfDay(zoneId)
        val samples = listOf(
            Task(
                id = 0,
                dateStartMillis = start.plusHours(9).toInstant().toEpochMilli(),
                dateFinishMillis = start.plusHours(10).toInstant().toEpochMilli(),
                name = "Утренний стендап",
                description = "Краткий синк команды.",
            ),
            Task(
                id = 0,
                dateStartMillis = start.plusHours(14).toInstant().toEpochMilli(),
                dateFinishMillis = start.plusHours(15).toInstant().toEpochMilli(),
                name = "My task",
                description = "Описание моего дела",
            ),
        )
        samples.forEach { insert(it) }
    }

    private fun TaskEntity.toDomain() = Task(
        id = id,
        dateStartMillis = dateStartMillis,
        dateFinishMillis = dateFinishMillis,
        name = name,
        description = description,
    )
}
