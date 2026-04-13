package com.diary.app.ui.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diary.app.domain.model.Task
import com.diary.app.service.HourSlot
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Строка почасовой сетки: интервал и блоки дел (кастомный компонент экрана на Compose).
 */
@Composable
fun HourTimelineRow(
    slot: HourSlot,
    zoneId: ZoneId,
    onTaskClick: (Task) -> Unit,
    modifier: Modifier = Modifier,
) {
    val timeFmt = DateTimeFormatter.ofPattern("HH:mm").withZone(zoneId)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = "${slot.labelStart}–${slot.labelEnd}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 10.dp),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (slot.tasks.isEmpty()) {
                Text(
                    text = "—",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            } else {
                slot.tasks.forEach { task ->
                    TaskBlockCard(
                        task = task,
                        timeFmt = timeFmt,
                        onClick = { onTaskClick(task) },
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskBlockCard(
    task: Task,
    timeFmt: DateTimeFormatter,
    onClick: () -> Unit,
) {
    val start = timeFmt.format(Instant.ofEpochMilli(task.dateStartMillis))
    val end = timeFmt.format(Instant.ofEpochMilli(task.dateFinishMillis))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                text = task.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = "$start – $end",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
            )
        }
    }
}
