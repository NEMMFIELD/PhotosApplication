package com.example.workmanager

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import java.util.concurrent.TimeUnit

class RandomPhotoWorkerRepository {
    private val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED)
        .build()
    val periodicWork = PeriodicWorkRequest.Builder(
        RandomPhotoWorker::class.java,
        50L,
        TimeUnit.MINUTES
    )
        .addTag("Work_Manager_Rnd")
        .setConstraints(constraints)
        .setInitialDelay(20L, TimeUnit.SECONDS)
        .build()
}
