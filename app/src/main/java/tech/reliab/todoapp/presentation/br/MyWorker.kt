package tech.reliab.todoapp.presentation.br

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.reliab.todoapp.R
import tech.reliab.todoapp.data.repository.TaskCategoryRepositoryImpl
import tech.reliab.todoapp.presentation.App
import java.util.Date
import javax.inject.Inject

class MyWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    init {
        App.get().appComponent.inject(this)
    }

    @Inject
    lateinit var repository: TaskCategoryRepositoryImpl

    override fun doWork(): Result {
        val context = applicationContext

        val time = Date()
        val oneHourLater = Date(time.time + 60 * 60 * 1000)
        CoroutineScope(Dispatchers.Main).launch {
            val list = repository.getActiveAlarms(time, oneHourLater)
            if (list.isNotEmpty()) {
                showNotification(
                    context,
                    "Задач в течении часа: ${list.size}",
                    "Не забудьте выполнить"
                )
            }
        }

        return Result.success()
    }

    private fun showNotification(context: Context, title: String, message: String) {
        val notificationBuilder = NotificationCompat.Builder(context, "default_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, notificationBuilder.build())
    }
}
