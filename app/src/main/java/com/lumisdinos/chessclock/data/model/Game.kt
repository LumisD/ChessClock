package com.lumisdinos.chessclock.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lumisdinos.chessclock.data.Constants.GAME

@Entity(tableName = GAME)
data class Game constructor(
    @PrimaryKey @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "system_millis") var systemMillis: Long = 0L,
    @ColumnInfo(name = "min") var min: Int = 0,
    @ColumnInfo(name = "sec") var sec: Int = 0,
    @ColumnInfo(name = "inc") var inc: Int = 0,
    @ColumnInfo(name = "is_first") var isFirst: Boolean = true,
    @ColumnInfo(name = "first_rest") var firstRest: Long = 0L,
    @ColumnInfo(name = "second_rest") var secondRest: Long = 0L
)