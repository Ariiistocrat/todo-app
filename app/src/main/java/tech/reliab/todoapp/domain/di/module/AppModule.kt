package tech.reliab.todoapp.domain.di.module

import dagger.Module

@Module(
    includes = [
        RepositoryModule::class,
        DatabaseModule::class
    ]
)

class AppModule