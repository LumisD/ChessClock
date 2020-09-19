package com.lumisdinos.chessclock.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lumisdinos.chessclock.data.Constants.GAME

@Entity(tableName = GAME)
data class Game constructor(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "system_millis") val systemMillis: Long = 0L,
    @ColumnInfo(name = "min") val min: Int = 0,
    @ColumnInfo(name = "sec") val sec: Int = 0,
    @ColumnInfo(name = "inc") val inc: Int = 0,
    @ColumnInfo(name = "is_first") val isFirst: Boolean = true,
    @ColumnInfo(name = "first_rest") val firstRest: Long = 0L,
    @ColumnInfo(name = "second_rest") val secondRest: Long = 0L
)