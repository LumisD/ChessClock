package com.lumisdinos.chessclock.data.repository

import com.lumisdinos.chessclock.data.DaoDB
import com.lumisdinos.chessclock.data.model.Game
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val daoDB: DaoDB
) : GameRepository {

    override suspend fun getGameCount(): Int {
        return daoDB.getGameCount()
    }

    override suspend fun getGameById(gameId: Int): Game? {
        return daoDB.getGameById(gameId)
    }

    override suspend fun insertGame(game: Game) {
        daoDB.insertGame(game)
    }

    override suspend fun deleteGame(gameId: Int): Int {
        return daoDB.deleteGame(gameId)
    }

    override suspend fun deleteAllGame(): Int {
        return daoDB.deleteAllGame()
    }

}