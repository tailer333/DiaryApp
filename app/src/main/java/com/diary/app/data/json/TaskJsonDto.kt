package com.diary.app.data.json

import com.diary.app.domain.model.Task
import com.google.gson.annotations.SerializedName

/**
 * Формат JSON по заданию. Поля [date_start] и [date_finish] — timestamp;
 * поддерживаются секунды и миллисекунды (значение меньше 10^12 считается секундами).
 */
data class TaskJsonDto(
    @SerializedName("id") val id: Long,
    @SerializedName("date_start") val dateStartRaw: String,
    @SerializedName("date_finish") val dateFinishRaw: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
) {
    fun toDomain(): Task = Task(
        id = id,
        dateStartMillis = parseTimestamp(dateStartRaw),
        dateFinishMillis = parseTimestamp(dateFinishRaw),
        name = name,
        description = description,
    )

    companion object {
        fun fromDomain(t: Task): TaskJsonDto = TaskJsonDto(
            id = t.id,
            dateStartRaw = t.dateStartMillis.toString(),
            dateFinishRaw = t.dateFinishMillis.toString(),
            name = t.name,
            description = t.description,
        )

        fun parseTimestamp(raw: String): Long {
            val v = raw.trim().toLong()
            return if (v < 1_000_000_000_000L) v * 1000L else v
        }
    }
}
