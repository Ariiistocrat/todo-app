package tech.reliab.todoapp.domain.model

import tech.reliab.todoapp.data.model.CategoryInfo

data class TaskCategoryInfoView(
    val taskInfo: TaskInfoView,
    val categoryInfo: List<CategoryInfo>
)
