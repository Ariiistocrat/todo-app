package tech.reliab.todoapp.domain.model.mapper

import tech.reliab.todoapp.data.model.TaskInfo
import tech.reliab.todoapp.domain.model.TaskInfoView

class TaskInfoMapper : BaseMapper<TaskInfo, TaskInfoView> {
    override fun mapToDomain(data: TaskInfo): TaskInfoView {
        return TaskInfoView(
            id = data.id,
            description = data.description,
            date = data.date,
            priority = data.priority,
            status = data.status,
            category = data.category
        )
    }

    override fun mapToData(data: TaskInfoView): TaskInfo {
        return TaskInfo(
            id = data.id,
            description = data.description,
            date = data.date,
            priority = data.priority,
            status = data.status,
            category = data.category
        )
    }
}