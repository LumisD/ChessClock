package com.lumisdinos.chessclock.data

import androidx.room.*
import com.lumisdinos.chessclock.data.model.Game

@Dao
interface DaoDB {

    //Game

    @Query("SELECT COUNT(*) FROM game")
    fun getGameCount(): Int

    @Query("SELECT * FROM game WHERE id = :gameId")
    fun getGameById(gameId: Int): Game?

    @Query("SELECT * FROM game LIMIT 1")
    fun getFirstGame(): Game?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(row20: Game)

    @Query("DELETE FROM game WHERE id = :gameId")
    fun deleteGame(gameId: Int): Int

    @Query("DELETE FROM game")
    fun deleteAllGame(): Int

}