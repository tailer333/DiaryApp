package com.diary.app.service

import com.diary.app.domain.model.Task
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class TaskScheduleServiceTest {

    private val zone = ZoneId.of("UTC")
    private val service = TaskScheduleService(zone)

    @Test
    fun `task overlapping two hour slots appears in both`() {
        val day = LocalDate.of(2024, 6, 15)
        val dayStart = day.atStartOfDay(zone)
        val start = dayStart.plusHours(14).plusMinutes(30).toInstant().toEpochMilli()
        val end = dayStart.plusHours(15).plusMinutes(30).toInstant().toEpochMilli()
        val task = Task(1, start, end, "Meeting", "desc")
        val slots = service.buildDaySchedule(day, listOf(task))
        assertTrue(slots[14].tasks.contains(task))
        assertTrue(slots[15].tasks.contains(task))
        assertTrue(slots[13].tasks.isEmpty())
        assertEquals("24:00", slots[23].labelEnd)
    }
}
