package tech.reliab.todoapp.domain.model.mapper

import tech.reliab.todoapp.data.model.TaskCategoryInfo
import tech.reliab.todoapp.domain.model.TaskCategoryInfoView

class TaskCategoryInfoMapper : BaseMapper<TaskCategoryInfo, TaskCategoryInfoView> {
    override fun mapToDomain(data: TaskCategoryInfo): TaskCategoryInfoView {
        return TaskCategoryInfoView(
            taskInfo = TaskInfoMapper().mapToDomain(data.taskInfo),
            categoryInfo = data.categoryInfo
        )
    }

    override fun mapToData(data: TaskCategoryInfoView): TaskCategoryInfo {
        return TaskCategoryInfo(
            taskInfo = TaskInfoMapper().mapToData(data.taskInfo),
            categoryInfo = data.categoryInfo
        )
    }
}