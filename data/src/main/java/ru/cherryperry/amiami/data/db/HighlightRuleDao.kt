package ru.cherryperry.amiami.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
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
