package com.diary.app.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.diary.app.data.TaskRepository
import com.diary.app.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

data class CreateTaskUiState(
    val name: String = "",
    val date: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.of(9, 0),
    val endTime: LocalTime = LocalTime.of(10, 0),
    val description: String = "",
    val saved: Boolean = false,
    val error: String? = null,
)

class CreateTaskViewModel(
    private val repository: TaskRepository,
    private val zoneId: ZoneId = ZoneId.systemDefault(),
) : ViewModel() {

    private val _state = MutableStateFlow(CreateTaskUiState())
    val state: StateFlow<CreateTaskUiState> = _state.asStateFlow()

    fun setName(value: String) {
        _state.value = _state.value.copy(name = value, error = null)
    }

    fun setDate(value: LocalDate) {
        _state.value = _state.value.copy(date = value, error = null)
    }

    fun setStartTime(value: LocalTime) {
        _state.value = _state.value.copy(startTime = value, error = null)
    }

    fun setEndTime(value: LocalTime) {
        _state.value = _state.value.copy(endTime = value, error = null)
    }

    fun setDescription(value: String) {
        _state.value = _state.value.copy(description = value, error = null)
    }

    fun save() {
        val s = _state.value
        val name = s.name.trim()
        if (name.isEmpty()) {
            _state.value = s.copy(error = "Укажите название")
            return
        }
        val start = s.date.atTime(s.startTime).atZone(zoneId).toInstant().toEpochMilli()
        val end = s.date.atTime(s.endTime).atZone(zoneId).toInstant().toEpochMilli()
        if (end <= start) {
            _state.value = s.copy(error = "Время окончания должно быть позже начала")
            return
        }
        viewModelScope.launch {
            repository.insert(
                Task(
                    id = 0,
                    dateStartMillis = start,
                    dateFinishMillis = end,
                    name = name,
                    description = s.description.trim(),
                ),
            )
            _state.value = s.copy(saved = true, error = null)
        }
    }

    class Factory(
        private val repository: TaskRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(CreateTaskViewModel::class.java))
            return CreateTaskViewModel(repository) as T
        }
    }
}
