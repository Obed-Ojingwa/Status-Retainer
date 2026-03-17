package com.nerdpace.statussaver.di

// App Module




import android.content.Context
import androidx.room.Room
import com.nerdpace.statussaver.data.local.StatusDatabase
import com.nerdpace.statussaver.data.local.dao.StatusMediaDao
import com.nerdpace.statussaver.data.repository.StatusRepositoryImpl
import com.nerdpace.statussaver.domain.repository.StatusRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStatusDatabase(
        @ApplicationContext context: Context
    ): StatusDatabase {
        return Room.databaseBuilder(
            context,
            StatusDatabase::class.java,
            "status_saver_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }



    @Provides
    @Singleton
    fun provideStatusMediaDao(database: StatusDatabase): StatusMediaDao {
        return database.statusMediaDao()
    }

    @Provides
    @Singleton
    fun provideStatusRepository(
        @ApplicationContext context: Context,
        dao: StatusMediaDao
    ): StatusRepository {
        return StatusRepositoryImpl(context, dao)
    }
}