package com.nerdpace.statussaver.worker

// Cleanup Worker


import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.nerdpace.statussaver.domain.repository.StatusRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


@HiltWorker
class CleanupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: StatusRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            val deletedCount = repository.cleanupExpiredMedia()

            Result.success(
                workDataOf(
                    KEY_DELETED_COUNT to deletedCount,
                    KEY_TIMESTAMP to System.currentTimeMillis()
                )
            )
        } catch (e: Exception) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure(
                    workDataOf(KEY_ERROR to (e.message ?: "Unknown error"))
                )
            }
        }
    }

    companion object {
        const val KEY_DELETED_COUNT = "deleted_count"
        const val KEY_TIMESTAMP = "timestamp"
        const val KEY_ERROR = "error"
        const val WORK_NAME = "status_cleanup_work"
    }
}