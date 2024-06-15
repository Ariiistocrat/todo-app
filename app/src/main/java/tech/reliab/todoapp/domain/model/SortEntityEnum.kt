package tech.reliab.todoapp.domain.model

enum class SortEntityEnum(val text: String) {
    DateMinus("Дата по убыванию"), DatePlus("Дата по возрастанию"), Priority("По приоритету")
}