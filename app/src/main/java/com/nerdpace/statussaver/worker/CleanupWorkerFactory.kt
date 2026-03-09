package com.nerdpace.statussaver.worker


// Worker Factory for Dependency Injection


import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.nerdpace.statussaver.domain.repository.StatusRepository

class CleanupWorkerFactory(
    private val repository: StatusRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            CleanupWorker::class.java.name -> {
                CleanupWorker(appContext, workerParameters, repository)
            }
            else -> null
        }
    }
}