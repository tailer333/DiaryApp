package com.diary.app.data.json

import org.junit.Assert.assertEquals
import org.junit.Test

class TaskJsonDtoTest {

    @Test
    fun `parseTimestamp treats compact values as seconds and large as millis`() {
        assertEquals(147_600_000_000L, TaskJsonDto.parseTimestamp("147600000"))
        val ms = 1_700_000_000_000L
        assertEquals(ms, TaskJsonDto.parseTimestamp(ms.toString()))
    }
}
