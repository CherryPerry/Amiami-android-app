package ru.cherryperry.amiami.data.db

import android.arch.persistence.room.Room
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import ru.cherryperry.amiami.data.R

@RunWith(RobolectricTestRunner::class)
class NoDatabaseToDatabaseMigrationTest {

    private val scheduler = Schedulers.single()

    @Test
    fun testMigrationEmpty() {
        val migration = NoDatabaseToDatabaseMigration(RuntimeEnvironment.application, scheduler)
        val appDatabase = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.application, AppDatabase::class.java)
            .addCallback(migration)
            .build()
        val migrationTest = migration
            .migrateCompletable(appDatabase)
            .test()
        // onCreate called on first db call
        appDatabase
            .highlightRuleDao()
            .get()
            .firstElement()
            .subscribeOn(scheduler)
            .test()
            .await()
            .assertValue(emptyList())
            .dispose()
        migrationTest
            .await()
            .assertComplete()
            .dispose()
        appDatabase.close()
    }

    @Test
    fun testMigrationFilled() {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application)
        preferences.edit {
            putStringSet(RuntimeEnvironment.application.getString(R.string.key_favorites), setOf("test1", "test2"))
        }
        val migration = NoDatabaseToDatabaseMigration(RuntimeEnvironment.application, scheduler)
        val appDatabase = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.application, AppDatabase::class.java)
            .addCallback(migration)
            .build()
        val migrationTest = migration
            .migrateCompletable(appDatabase)
            .test()
        // onCreate called on first db call
        appDatabase.highlightRuleDao().get()
            .firstElement()
            .subscribeOn(scheduler)
            .test()
            .await()
            .assertValue(
                listOf(
                    DbHighlightRule(1, "test2", false),
                    DbHighlightRule(2, "test1", false)))
            .dispose()
        migrationTest
            .await()
            .assertComplete()
            .dispose()
        appDatabase.close()
    }
}
