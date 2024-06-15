package tech.reliab.todoapp.presentation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import tech.reliab.todoapp.domain.di.AppComponent
import tech.reliab.todoapp.domain.di.DaggerAppComponent
import tech.reliab.todoapp.presentation.br.MyWorker
import java.util.concurrent.TimeUnit

class App : Application() {

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        setupPeriodicWork()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "default_channel",
            "Default Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    private fun setupPeriodicWork() {
        val repeatingWorkRequest = PeriodicWorkRequest.Builder(
            MyWorker::class.java,
            15, TimeUnit.MINUTES // Период выполнения задачи
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ExampleWorker",
            ExistingPeriodicWorkPolicy.KEEP, // Используйте KEEP, чтобы не создавать дубликаты
            repeatingWorkRequest
        )
    }

    companion object {
        lateinit var instance: App
        fun get(): App {
            return instance
        }
    }

    open fun initializeComponent(): AppComponent {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        return DaggerAppComponent.factory().create(this)
    }
}