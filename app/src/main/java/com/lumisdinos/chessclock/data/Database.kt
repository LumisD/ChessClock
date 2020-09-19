package com.lumisdinos.chessclock.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lumisdinos.chessclock.data.model.Game

@Database(
    entities = [
        Game::class

    ], version = 1, exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun daoDB(): DaoDB

}