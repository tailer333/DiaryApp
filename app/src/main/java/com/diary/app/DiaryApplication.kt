package com.diary.app

import android.app.Application
import com.diary.app.data.TaskRepository
import com.diary.app.data.local.AppDatabase

class DiaryApplication : Application() {

    lateinit var repository: TaskRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.create(this)
        repository = TaskRepository(db.taskDao())
    }
}
