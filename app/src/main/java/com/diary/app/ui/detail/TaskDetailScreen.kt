package com.diary.app.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    viewModel: TaskDetailViewModel,
    zoneId: ZoneId,
    onBack: () -> Unit,
) {
    val task by viewModel.task.collectAsState()
    val dtFmt = rememberFormatter(zoneId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Дело") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            when (val t = task) {
                null -> Text("Загрузка…", style = MaterialTheme.typography.bodyLarge)
                else -> {
                    Text(t.name, style = MaterialTheme.typography.headlineSmall)
                    val start = dtFmt.format(Instant.ofEpochMilli(t.dateStartMillis))
                    val end = dtFmt.format(Instant.ofEpochMilli(t.dateFinishMillis))
                    Text(
                        "Дата и время: $start — $end",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        "Описание",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        t.description.ifBlank { "Нет описания" },
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberFormatter(zoneId: ZoneId): DateTimeFormatter {
    return remember(zoneId) {
        DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm").withZone(zoneId)
    }
}
