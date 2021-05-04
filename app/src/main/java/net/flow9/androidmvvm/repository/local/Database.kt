package net.flow9.androidmvvm.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import net.flow9.androidmvvm.repository.model.response.GithubUser
import javax.inject.Singleton

@Database(entities = [GithubUser::class], version = 1, exportSchema = false)
abstract class RoomDB : RoomDatabase() {
    abstract fun githubUserDao(): GithubUserDao
}

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun getDatabase(@ApplicationContext context:Context): RoomDB = buildDatabase(context)

    private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, RoomDB::class.java, "android")
                    .fallbackToDestructiveMigration()
                    .build()

    @Provides
    @Singleton
    fun provideChannelDao(roomDB: RoomDB): GithubUserDao {
        return roomDB.githubUserDao()
    }
}