package ru.cherryperry.amiami.data.db

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun appDatabase(
        context: Context,
        noDatabaseToDatabaseMigration: NoDatabaseToDatabaseMigration
    ): AppDatabase {
        val database = Room.databaseBuilder(context, AppDatabase::class.java, "app")
            .addCallback(noDatabaseToDatabaseMigration)
            .build()
        noDatabaseToDatabaseMigration.migrate(database)
        return database
    }

    @Provides
    fun highlightDao(appDatabase: AppDatabase) = appDatabase.highlightRuleDao()

    @Provides
    @DatabaseScheduler
    fun scheduler() = Schedulers.from(Executors.newSingleThreadExecutor())
}
