package com.diary.app.ui.schedule

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diary.app.domain.model.Task
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel,
    zoneId: ZoneId,
    onOpenTask: (Long) -> Unit,
    onCreateTask: () -> Unit,
) {
    val selectedDay by viewModel.selectedDay.collectAsState()
    val slots by viewModel.hourSlots.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    val titleFmt = remember(zoneId) {
        DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy").withZone(zoneId)
    }
    val dayTitle = remember(selectedDay, titleFmt, zoneId) {
        titleFmt.format(selectedDay.atStartOfDay(zoneId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Дела на день") },
                actions = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Выбрать дату")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateTask) {
                Icon(Icons.Default.Add, contentDescription = "Новое дело")
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(bottom = 8.dp),
        ) {
            item {
                Text(
                    text = dayTitle,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
            items(slots, key = { it.hour }) { slot ->
                HourTimelineRow(
                    slot = slot,
                    zoneId = zoneId,
                    onTaskClick = { task: Task -> onOpenTask(task.id) },
                )
            }
        }
    }

    if (showDatePicker) {
        val initialMillis = selectedDay.atStartOfDay(zoneId).toInstant().toEpochMilli()
        val dateState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        dateState.selectedDateMillis?.let { millis ->
                            val day = Instant.ofEpochMilli(millis).atZone(zoneId).toLocalDate()
                            viewModel.selectDay(day)
                        }
                        showDatePicker = false
                    },
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
            },
        ) {
            DatePicker(state = dateState)
        }
    }
}
