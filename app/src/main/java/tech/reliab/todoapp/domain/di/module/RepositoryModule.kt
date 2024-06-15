package tech.reliab.todoapp.domain.di.module

import dagger.Module
import dagger.Provides
import tech.reliab.todoapp.data.db.TaskCategoryDao
import tech.reliab.todoapp.data.repository.TaskCategoryRepositoryImpl
import tech.reliab.todoapp.domain.repository.TaskCategoryRepository
import javax.inject.Singleton

@Module
open class RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(
        dao: TaskCategoryDao
    ): TaskCategoryRepository = TaskCategoryRepositoryImpl(dao)

}