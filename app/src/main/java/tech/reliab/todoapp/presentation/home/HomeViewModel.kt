package tech.reliab.todoapp.presentation.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tech.reliab.todoapp.domain.model.SortEntityEnum
import tech.reliab.todoapp.domain.model.TaskCategoryInfoView
import tech.reliab.todoapp.domain.model.TaskInfoView
import tech.reliab.todoapp.domain.repository.TaskCategoryRepository
import tech.reliab.todoapp.presentation.App
import javax.inject.Inject

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    init {
        App.get().appComponent.inject(this)
    }

    @Inject
    lateinit var repository: TaskCategoryRepository

    var choiceSortType: SortEntityEnum = SortEntityEnum.DateMinus

    fun getUncompletedTask(): Flow<List<TaskCategoryInfoView>> {
        val items = repository.getUncompletedTask()
        return when (choiceSortType) {
            SortEntityEnum.DateMinus -> items.map {
                it.sortedByDescending { task -> task.taskInfo.date }
            }

            SortEntityEnum.DatePlus -> items.map {
                it.sortedBy { task -> task.taskInfo.date }
            }

            else -> items.map {
                it.sortedByDescending { task -> task.taskInfo.priority }
            }

        }
    }

    fun updateTaskStatus(task: TaskInfoView) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTaskStatus(task)
        }
    }

}