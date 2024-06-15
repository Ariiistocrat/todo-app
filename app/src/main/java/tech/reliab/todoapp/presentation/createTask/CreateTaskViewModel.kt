package tech.reliab.todoapp.presentation.createTask

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.reliab.todoapp.domain.model.TaskInfoView
import tech.reliab.todoapp.domain.repository.TaskCategoryRepository
import tech.reliab.todoapp.presentation.App
import java.util.Date
import javax.inject.Inject
import kotlin.math.abs

class CreateTaskViewModel(app: Application) : AndroidViewModel(app) {

    init {
        App.get().appComponent.inject(this)
    }

    @Inject
    lateinit var repository: TaskCategoryRepository

    var category: String? = null
    var date: Date? = null
    var inUpdateMode = false
    var id: Int? = null

    fun createTask(description: String, priority: Int? = null) {
        val task = TaskInfoView(
            id = id ?: abs((0..999999999999).random()).toInt(),
            description = description,
            date = date ?: Date(),
            priority = priority ?: 0,
            status = false,
            category = category ?: ""
        )
        if (inUpdateMode)
            updateTaskAndAddCategory(task)
        else
            insertTaskAndCategory(
                task
            )
    }

    private fun updateTaskAndAddCategory(taskInfo: TaskInfoView) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTaskAndAddCategory(taskInfo)
        }
    }

    private fun insertTaskAndCategory(taskInfo: TaskInfoView) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTaskAndCategory(taskInfo)
        }
    }
}