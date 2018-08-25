package ru.cherryperry.amiami.data.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "highlight")
data class DbHighlightRule(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(name = "value")
    var value: String = "",
    @ColumnInfo(name = "regexp")
    var regex: Boolean = false
)
