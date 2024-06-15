package tech.reliab.todoapp.data.repository

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import tech.reliab.todoapp.data.db.TaskCategoryDao
import tech.reliab.todoapp.data.model.TaskInfo
import tech.reliab.todoapp.domain.model.TaskCategoryInfoView
import tech.reliab.todoapp.domain.model.TaskInfoView
import tech.reliab.todoapp.domain.model.mapper.TaskCategoryInfoMapper
import tech.reliab.todoapp.domain.model.mapper.TaskInfoMapper
import tech.reliab.todoapp.domain.repository.TaskCategoryRepository
import java.util.Date
import javax.inject.Inject

class TaskCategoryRepositoryImpl @Inject constructor(private val taskCategoryDao: TaskCategoryDao) :
    TaskCategoryRepository {
    private val taskInfoMapper = TaskInfoMapper()
    private val taskCategoryInfoMapper = TaskCategoryInfoMapper()

    override suspend fun updateTaskStatus(task: TaskInfoView): Int {
        return taskCategoryDao.updateTaskStatus(TaskInfoMapper().mapToData(task))
    }

    override suspend fun insertTaskAndCategory(
        taskInfo: TaskInfoView
    ) {
        taskCategoryDao.insertTaskAndCategory(
            taskInfoMapper.mapToData(taskInfo)
        )
    }

    override suspend fun updateTaskAndAddCategory(
        taskInfo: TaskInfoView
    ) {
        taskCategoryDao.updateTaskAndAddCategory(
            taskInfoMapper.mapToData(taskInfo)
        )
    }

    override fun getUncompletedTask(): Flow<List<TaskCategoryInfoView>> =
        taskCategoryDao.getUncompletedTask()
            .map { flow -> flow.map { taskCategoryInfoMapper.mapToDomain(it) } }

    override fun getCompletedTask(): Flow<List<TaskCategoryInfoView>> =
        taskCategoryDao.getCompletedTask()
            .map { flow -> flow.map { taskCategoryInfoMapper.mapToDomain(it) } }

    override suspend fun getActiveAlarms(
        currentTime: Date,
        oneHourLater: Date
    ): List<TaskInfoView> {
        var list: List<TaskInfo>
        coroutineScope {
            list = withContext(IO) { taskCategoryDao.getActiveAlarms(currentTime, currentTime) }
        }
        return list.map { taskInfoMapper.mapToDomain(it) }
    }
}