package tech.reliab.todoapp.domain.di.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import tech.reliab.todoapp.data.db.TaskCategoryDao
import tech.reliab.todoapp.data.db.TaskDatabase
import javax.inject.Singleton

@Module
open class DatabaseModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(app, TaskDatabase::class.java, "task_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(
        taskDatabase: TaskDatabase
    ): TaskCategoryDao = taskDatabase.getTaskCategoryDao()

}