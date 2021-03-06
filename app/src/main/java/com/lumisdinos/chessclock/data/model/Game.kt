package com.lumisdinos.chessclock.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lumisdinos.chessclock.data.Constants.GAME

@Entity(tableName = GAME)
data class Game constructor(
    @PrimaryKey @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "system_millis") var systemMillis: Long = 0L,
    @ColumnInfo(name = "tick_system_millis") var tickSystemMillis: Long = 0L,
    @ColumnInfo(name = "min") var min: Int = 0,
    @ColumnInfo(name = "sec") var sec: Int = 0,
    @ColumnInfo(name = "inc") var inc: Int = 0,
    @ColumnInfo(name = "white_rest") var whiteRest: Long = 0L,
    @ColumnInfo(name = "black_rest") var blackRest: Long = 0L,

    @ColumnInfo(name = "is_white_first") var isBottomFirst: Boolean = true,
    @ColumnInfo(name = "is_white_player_moving") var isWhitePlayerThinking: Boolean = true,
    @ColumnInfo(name = "is_paused") var isPaused: Boolean = false,
    @ColumnInfo(name = "is_game_finished") var isGameFinished: Boolean = false
)