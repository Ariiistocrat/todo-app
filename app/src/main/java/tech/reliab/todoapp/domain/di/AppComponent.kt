package tech.reliab.todoapp.domain.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import tech.reliab.todoapp.domain.di.module.AppModule
import tech.reliab.todoapp.presentation.br.MyWorker
import tech.reliab.todoapp.presentation.completedTasks.CompletedTasksViewModel
import tech.reliab.todoapp.presentation.createTask.CreateTaskViewModel
import tech.reliab.todoapp.presentation.home.HomeViewModel
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class
    ]
)

interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): AppComponent
    }

    fun inject(viewModel: CreateTaskViewModel)
    fun inject(viewModel: CompletedTasksViewModel)
    fun inject(viewModel: HomeViewModel)
    fun inject(br: MyWorker)

}
