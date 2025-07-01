package com.life.totally.great.data.scheduler

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.life.totally.great.data.datasources.local.location.LocationUpdateWorker
import com.life.totally.great.domain.scheduler.LocationUpdateScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationUpdateSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationUpdateScheduler {
    override fun schedulePeriodic() {
        cancel()
        val req = PeriodicWorkRequestBuilder<LocationUpdateWorker>(15, TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniquePeriodicWork(
            LocationUpdateWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            req
        )
    }

    // not Used now, but may be used in future
    override fun cancel() {
        WorkManager.getInstance(context).cancelAllWork()
    }
}
