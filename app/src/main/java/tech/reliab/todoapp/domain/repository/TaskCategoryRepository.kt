package tech.reliab.todoapp.domain.repository

import kotlinx.coroutines.flow.Flow
import tech.reliab.todoapp.domain.model.TaskCategoryInfoView
import tech.reliab.todoapp.domain.model.TaskInfoView
import java.util.Date

interface TaskCategoryRepository {
    suspend fun updateTaskStatus(task: TaskInfoView): Int
    suspend fun insertTaskAndCategory(taskInfo: TaskInfoView)
    suspend fun updateTaskAndAddCategory(taskInfo: TaskInfoView)
    fun getUncompletedTask(): Flow<List<TaskCategoryInfoView>>
    fun getCompletedTask(): Flow<List<TaskCategoryInfoView>>
    suspend fun getActiveAlarms(currentTime: Date, oneHourLater: Date): List<TaskInfoView>
}