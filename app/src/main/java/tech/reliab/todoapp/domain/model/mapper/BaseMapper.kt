package tech.reliab.todoapp.domain.model.mapper

interface BaseMapper<D, T> {

    fun mapToDomain(data: D): T

    fun mapToData(data: T): D
}