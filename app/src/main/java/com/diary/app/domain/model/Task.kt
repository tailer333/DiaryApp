package com.diary.app.domain.model

data class Task(
    val id: Long,
    val dateStartMillis: Long,
    val dateFinishMillis: Long,
    val name: String,
    val description: String,
)
