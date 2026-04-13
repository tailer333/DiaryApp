package com.diary.app.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.diary.app.data.TaskRepository
import com.diary.app.service.HourSlot
import com.diary.app.service.TaskScheduleService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class ScheduleViewModel(
    private val repository: TaskRepository,
    private val scheduleService: TaskScheduleService,
) : ViewModel() {

    private val _selectedDay = MutableStateFlow(LocalDate.now())
    val selectedDay: StateFlow<LocalDate> = _selectedDay.asStateFlow()

    val hourSlots: StateFlow<List<HourSlot>> = _selectedDay
        .flatMapLatest { day ->
            repository.observeTasksForDay(day).map { tasks ->
                scheduleService.buildDaySchedule(day, tasks)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch { repository.seedSampleTasksIfEmpty() }
    }

    fun selectDay(day: LocalDate) {
        _selectedDay.value = day
    }

    class Factory(
        private val repository: TaskRepository,
        private val scheduleService: TaskScheduleService,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(ScheduleViewModel::class.java))
            return ScheduleViewModel(repository, scheduleService) as T
        }
    }
}
