package ru.cherryperry.amiami.data.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.cherryperry.amiami.core.createBackgroundThreadScheduler
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
        //noinspection CheckResult
        noDatabaseToDatabaseMigration
            .migrateCompletable(database)
            .subscribe({}, {})
        return database
    }

    @Provides
    fun highlightDao(appDatabase: AppDatabase) = appDatabase.highlightRuleDao()

    @Provides
    @Singleton
    @DatabaseScheduler
    fun scheduler() = createBackgroundThreadScheduler("DatabaseThread")
}
