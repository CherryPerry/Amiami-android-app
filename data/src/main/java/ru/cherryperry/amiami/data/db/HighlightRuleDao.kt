package ru.cherryperry.amiami.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import io.reactivex.Flowable

@Dao
interface HighlightRuleDao {

    @Query("select * from highlight")
    fun get(): Flowable<List<DbHighlightRule>>

    @Insert
    fun insert(highlightRule: DbHighlightRule)

    @Insert
    fun insert(list: List<DbHighlightRule>)

    @Delete
    fun delete(highlightRule: DbHighlightRule)

    @Query("delete from highlight")
    fun deleteAll()

    @Transaction
    fun replace(list: List<DbHighlightRule>) {
        deleteAll()
        insert(list)
    }
}
