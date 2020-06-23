package ru.cherryperry.amiami.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "highlight")
data class DbHighlightRule(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(name = "value")
    var value: String = "",
    @ColumnInfo(name = "regexp")
    var regex: Boolean = false
)
