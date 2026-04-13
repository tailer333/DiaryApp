package com.diary.app.service

import com.diary.app.domain.model.Task
import java.time.LocalDate
import java.time.ZoneId

data class HourSlot(
    val hour: Int,
    val labelStart: String,
    val labelEnd: String,
    val tasks: List<Task>,
)

/**
 * Сервисный слой: подготовка почасовой сетки дня и сопоставление дел с интервалами.
 */
class TaskScheduleService(
    private val zoneId: ZoneId = ZoneId.systemDefault(),
) {

    fun buildDaySchedule(selectedDay: LocalDate, tasks: List<Task>): List<HourSlot> {
        val dayStart = selectedDay.atStartOfDay(zoneId)
        return (0 until HOURS_PER_DAY).map { hour ->
            val slotStart = dayStart.plusHours(hour.toLong())
            val slotEnd = slotStart.plusHours(1)
            val slotStartMillis = slotStart.toInstant().toEpochMilli()
            val slotEndMillis = slotEnd.toInstant().toEpochMilli()
            val overlapping = tasks.filter { overlaps(it, slotStartMillis, slotEndMillis) }
            HourSlot(
                hour = hour,
                labelStart = String.format("%02d:00", hour),
                labelEnd = if (hour == HOURS_PER_DAY - 1) "24:00" else String.format("%02d:00", hour + 1),
                tasks = overlapping,
            )
        }
    }

    private fun overlaps(task: Task, slotStartMillis: Long, slotEndMillis: Long): Boolean =
        task.dateStartMillis < slotEndMillis && task.dateFinishMillis > slotStartMillis

    companion object {
        const val HOURS_PER_DAY = 24
    }
}
