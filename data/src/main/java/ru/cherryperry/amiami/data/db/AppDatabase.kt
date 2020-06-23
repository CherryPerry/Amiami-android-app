package ru.cherryperry.amiami.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DbHighlightRule::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun highlightRuleDao(): HighlightRuleDao
}
