package ru.cherryperry.amiami.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [DbHighlightRule::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun highlightRuleDao(): HighlightRuleDao
}