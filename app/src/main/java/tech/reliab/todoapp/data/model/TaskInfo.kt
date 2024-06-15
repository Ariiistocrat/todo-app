package tech.reliab.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity(tableName = "taskInfo")
data class TaskInfo(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var description: String,
    var date: Date,
    var priority: Int,
    var status: Boolean,
    var category: String
) : Serializable

