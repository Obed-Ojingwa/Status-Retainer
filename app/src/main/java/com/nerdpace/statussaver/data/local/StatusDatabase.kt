// Room Database
package com.nerdpace.statussaver.data.local


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nerdpace.statussaver.data.local.dao.StatusMediaDao
import com.nerdpace.statussaver.data.local.entity.StatusMediaEntity

@Database(
    entities = [StatusMediaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StatusDatabase : RoomDatabase() {

    abstract fun statusMediaDao(): StatusMediaDao

    companion object {
        @Volatile
        private var INSTANCE: StatusDatabase? = null

        fun getInstance(context: Context): StatusDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StatusDatabase::class.java,
                    "status_saver_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}