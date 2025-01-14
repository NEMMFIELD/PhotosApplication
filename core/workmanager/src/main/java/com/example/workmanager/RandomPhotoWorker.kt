package com.example.workmanager

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.photos.api.PhotosApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class RandomPhotoWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val api: PhotosApi
) : CoroutineWorker(context, workerParameters) {
    val title = context.getString(R.string.notification_title)
    val message = context.getString(R.string.notification_message)
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext kotlin.runCatching {
            val photos = api.getRandomPhotos(20)
            Log.d("Work Manager", "Works")
            showNotification(title, message)
            Result.success()
        }.getOrElse {
            Log.d("WorkManager", "Error in work execution")
            Result.failure()
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Log.d("Notification", "Notification Manager created")
        val notification = NotificationCompat.Builder(applicationContext, "WORK_MANAGER_CHANNEL")
            .setSmallIcon(R.drawable.unsplash)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(createPendingIntent(applicationContext))
            .setAutoCancel(true)
            .build()
        Log.d("Notification", "Notification sent")
        notificationManager.notify(1, notification)
    }

    private fun createPendingIntent(context: Context): PendingIntent {
        val intent = Intent(
            applicationContext,
            Class.forName("com.example.photosapplication.MainActivity")
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("ACTION", "REFRESH_PHOTOS")
        }

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}





