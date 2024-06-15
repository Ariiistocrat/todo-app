package tech.reliab.todoapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tech.reliab.todoapp.data.model.TaskCategoryInfo
import tech.reliab.todoapp.data.model.TaskInfo
import java.util.Date

@Dao
interface TaskCategoryDao {
    @Insert
    suspend fun insertTask(task: TaskInfo): Long

    @Update
    suspend fun updateTaskStatus(task: TaskInfo): Int

    @Transaction
    suspend fun insertTaskAndCategory(taskInfo: TaskInfo) {
        insertTask(taskInfo)
    }

    @Transaction
    suspend fun updateTaskAndAddCategory(taskInfo: TaskInfo) {
        updateTaskStatus(taskInfo)
    }

    @Transaction
    @Query(
        "SELECT * " +
                "FROM taskInfo " +
                "WHERE status = 0 " +
                "ORDER BY date"
    )
    fun getUncompletedTask(): Flow<List<TaskCategoryInfo>>

    @Transaction
    @Query(
        "SELECT * " +
                "FROM taskInfo " +
                "WHERE status = 1 " +
                "ORDER BY date"
    )
    fun getCompletedTask(): Flow<List<TaskCategoryInfo>>

    @Query(
        "SELECT * " +
                "FROM taskInfo " +
                "WHERE date > :currentTime " +
                "AND date <= :oneHourLater"
    )
    suspend fun getActiveAlarms(currentTime: Date, oneHourLater: Date): List<TaskInfo>

}