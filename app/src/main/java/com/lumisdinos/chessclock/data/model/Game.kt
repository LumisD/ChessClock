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
    @ColumnInfo(name = "white_rest") var whiteRest: Long = 0L,
    @ColumnInfo(name = "black_rest") var blackRest: Long = 0L,

    @ColumnInfo(name = "paused_millis") var pausedMillis: Long = 0L,//full time when was paused
    @ColumnInfo(name = "paused_start_millis") var pausedStartMillis: Long = 0L,//last time it was paused

    @ColumnInfo(name = "is_white_first") var isWhiteFirst: Boolean = true,
    @ColumnInfo(name = "is_first_player_moving") var isFirstPlayerMoving: Boolean = true,
    @ColumnInfo(name = "is_paused") var isPaused: Boolean = false
)