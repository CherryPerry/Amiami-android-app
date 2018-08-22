package ru.cherryperry.amiami.data.db

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun appDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app").build()

    @Provides
    fun highlightDao(appDatabase: AppDatabase) = appDatabase.highlightRuleDao()
}
