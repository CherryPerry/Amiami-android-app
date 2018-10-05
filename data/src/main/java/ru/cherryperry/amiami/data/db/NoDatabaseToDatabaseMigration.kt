package ru.cherryperry.amiami.data.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.processors.BehaviorProcessor
import ru.cherryperry.amiami.data.R
import javax.inject.Inject

/**
 * Initial data is in [SharedPreferences].
 * After initialization we should put it in database.
 */
class NoDatabaseToDatabaseMigration @Inject constructor(
    context: Context,
    @DatabaseScheduler private val scheduler: Scheduler
) : RoomDatabase.Callback() {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val key = context.getString(R.string.key_favorites)
    private val createdProcessor = BehaviorProcessor.create<Boolean>()
    private val appDatabaseProcessor = BehaviorProcessor.create<AppDatabase>()

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        createdProcessor.onNext(true)
    }

    fun migrateCompletable(appDatabase: AppDatabase): Completable {
        appDatabaseProcessor.onNext(appDatabase)
        return Flowable.zip(createdProcessor, appDatabaseProcessor,
            BiFunction<Boolean, AppDatabase, AppDatabase> { _, db -> db })
            .firstElement()
            .observeOn(scheduler)
            .flatMapCompletable { db ->
                val oldData = preferences.getStringSet(key, emptySet())
                if (oldData != null && oldData.isNotEmpty()) {
                    db.highlightRuleDao().insert(oldData.map { DbHighlightRule(null, it, false) })
                    preferences.edit { remove(key) }
                }
                Completable.complete()
            }
    }
}
