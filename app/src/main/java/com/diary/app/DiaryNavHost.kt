package com.diary.app

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.diary.app.data.TaskRepository
import com.diary.app.service.TaskScheduleService
import com.diary.app.ui.create.CreateTaskScreen
import com.diary.app.ui.create.CreateTaskViewModel
import com.diary.app.ui.detail.TaskDetailScreen
import com.diary.app.ui.detail.TaskDetailViewModel
import com.diary.app.ui.schedule.ScheduleScreen
import com.diary.app.ui.schedule.ScheduleViewModel
import java.time.ZoneId

private const val ROUTE_SCHEDULE = "schedule"
private const val ROUTE_CREATE = "create"
private const val ROUTE_TASK = "task/{taskId}"

@Composable
fun DiaryNavHost(
    repository: TaskRepository,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val zoneId = ZoneId.systemDefault()
    val scheduleViewModel: ScheduleViewModel = viewModel(
        factory = ScheduleViewModel.Factory(
            repository = repository,
            scheduleService = TaskScheduleService(zoneId),
        ),
    )

    Surface(modifier = modifier) {
        NavHost(navController = navController, startDestination = ROUTE_SCHEDULE) {
            composable(ROUTE_SCHEDULE) {
                ScheduleScreen(
                    viewModel = scheduleViewModel,
                    zoneId = zoneId,
                    onOpenTask = { id -> navController.navigate("task/$id") },
                    onCreateTask = { navController.navigate(ROUTE_CREATE) },
                )
            }
            composable(ROUTE_CREATE) {
                val vm: CreateTaskViewModel = viewModel(
                    factory = CreateTaskViewModel.Factory(repository),
                )
                CreateTaskScreen(
                    viewModel = vm,
                    zoneId = zoneId,
                    onBack = { navController.popBackStack() },
                    onSaved = { navController.popBackStack() },
                )
            }
            composable(
                route = ROUTE_TASK,
                arguments = listOf(
                    navArgument("taskId") { type = NavType.LongType },
                ),
            ) { entry ->
                val taskId = entry.arguments?.getLong("taskId") ?: return@composable
                val vm: TaskDetailViewModel = viewModel(
                    factory = TaskDetailViewModel.Factory(repository, taskId),
                )
                TaskDetailScreen(
                    viewModel = vm,
                    zoneId = zoneId,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
