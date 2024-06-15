package tech.reliab.todoapp.presentation.completedTasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.reliab.todoapp.domain.model.TaskCategoryInfoView
import tech.reliab.todoapp.domain.model.TaskInfoView
import tech.reliab.todoapp.domain.repository.TaskCategoryRepository
import tech.reliab.todoapp.presentation.App
import javax.inject.Inject

class CompletedTasksViewModel(app: Application) : AndroidViewModel(app) {

    init {
        App.get().appComponent.inject(this)
    }

    @Inject
    lateinit var repository: TaskCategoryRepository

    private val _tasks: MutableLiveData<List<TaskCategoryInfoView>> = MutableLiveData()
    val tasks: LiveData<List<TaskCategoryInfoView>> = _tasks

    fun getCompletedTask() {

        viewModelScope.launch(Dispatchers.IO) {
            repository.getCompletedTask().collect {
                _tasks.postValue(it)
            }
        }

    }

    fun updateTaskStatus(task: TaskInfoView) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTaskStatus(task)
        }
    }

}