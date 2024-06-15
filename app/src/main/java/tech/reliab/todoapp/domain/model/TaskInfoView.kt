package tech.reliab.todoapp.domain.model

import java.io.Serializable
import java.util.Date

data class TaskInfoView(
    var id: Int,
    var description: String,
    var date: Date,
    var priority: Int,
    var status: Boolean,
    var category: String
) : Serializable

