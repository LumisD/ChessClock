package com.lumisdinos.chessclock.data.repository

import com.lumisdinos.chessclock.data.model.Game

interface GameRepository {

    suspend fun getGameCount(): Int

    suspend fun getFirstGame(): Game?

    suspend fun resetGame(game: Game?)

    suspend fun getGameById(gameId: Int): Game?

    suspend fun insertIfNotEmptyTime(game: Game?)

    suspend fun deleteGame(gameId: Int): Int

    suspend fun deleteAllGame(): Int

}