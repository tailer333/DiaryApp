package com.diary.app.data.json

import com.diary.app.domain.model.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Сериализация списка дел в JSON в формате задания (см. [TaskJsonDto]).
 */
class TaskJsonCodec(private val gson: Gson = Gson()) {

    fun encodeList(tasks: List<Task>): String {
        val dtos = tasks.map { TaskJsonDto.fromDomain(it) }
        return gson.toJson(dtos)
    }

    fun decodeList(json: String): List<Task> {
        val type = object : TypeToken<List<TaskJsonDto>>() {}.type
        val list: List<TaskJsonDto> = gson.fromJson(json, type) ?: emptyList()
        return list.map { it.toDomain() }
    }
}
