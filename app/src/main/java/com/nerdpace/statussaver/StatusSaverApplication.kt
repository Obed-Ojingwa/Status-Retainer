package com.nerdpace.statussaver


// Application class to initialize WorkManager




import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.nerdpace.statussaver.worker.CleanupScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class StatusSaverApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        // Schedule periodic cleanup on app startup
        CleanupScheduler.scheduleCleanup(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}


//import android.app.Application
//import androidx.work.Configuration
//import com.nerdpace.statussaver.data.local.StatusDatabase
//import com.nerdpace.statussaver.data.repository.StatusRepositoryImpl
//import com.nerdpace.statussaver.worker.CleanupScheduler
//import com.nerdpace.statussaver.worker.CleanupWorkerFactory
//
//
//
//
//
//// Application Class
//
//
//
//import androidx.hilt.work.HiltWorkerFactory
//import dagger.hilt.android.HiltAndroidApp
//import javax.inject.Inject
//
//@HiltAndroidApp
//class StatusSaverApplication(override val workManagerConfiguration: Configuration) : Application(), Configuration.Provider {
//
//    // In a real app, use DI framework like Hilt or Koin
//   private val database by lazy { StatusDatabase.getInstance(this) }
//    private val repository by lazy { StatusRepositoryImpl(this, database.statusMediaDao()) }
//  // private val workerFactory by lazy { CleanupWorkerFactory(repository) }
//
//    @Inject
//    lateinit var workerFactory: HiltWorkerFactory
//
//    override fun onCreate() {
//        super.onCreate()
//
//        // Schedule periodic cleanup
//        CleanupScheduler.scheduleCleanup(this)
//    }
//
//    override fun getWorkManagerConfiguration(): Configuration {
//        return Configuration.Builder()
//            .setWorkerFactory(workerFactory)
//            .setMinimumLoggingLevel(android.util.Log.INFO)
//            .build()
//    }
//}

//class StatusSaverApplication : Application(), Configuration.Provider {
//
//    // In a real app, use DI framework like Hilt or Koin
//    private val database by lazy { StatusDatabase.getInstance(this) }
//    private val repository by lazy { StatusRepositoryImpl(this, database.statusMediaDao()) }
//    private val workerFactory by lazy { CleanupWorkerFactory(repository) }
//
//    override fun onCreate() {
//        super.onCreate()
//
//        // Schedule periodic cleanup on app startup
//        CleanupScheduler.scheduleCleanup(this)
//
//        // Optionally trigger immediate cleanup on app startup
//        // CleanupScheduler.triggerImmediateCleanup(this)
//    }
//
//    override fun getWorkManagerConfiguration(): Configuration {
//        return Configuration.Builder()
//            .setWorkerFactory(workerFactory)
//            .setMinimumLoggingLevel(android.util.Log.INFO)
//            .build()
//    }
//}