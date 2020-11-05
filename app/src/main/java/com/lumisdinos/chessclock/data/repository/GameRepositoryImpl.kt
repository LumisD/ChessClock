package com.lumisdinos.chessclock.data.repository

import com.lumisdinos.chessclock.data.AppConfig
import com.lumisdinos.chessclock.data.DaoDB
import com.lumisdinos.chessclock.data.model.Game
import timber.log.Timber
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val daoDB: DaoDB
) : GameRepository {


    override suspend fun getGameCount(): Int {
        return daoDB.getGameCount()
    }


    override suspend fun getFirstGame(): Game?{
        var game = daoDB.getFirstGame()

        if (game == null) {
            game = Game()
            game.min = AppConfig.min
            game.sec = AppConfig.sec
            game.inc = AppConfig.inc
            game.whiteRest = (AppConfig.min * 60 + AppConfig.sec) * 1000L
            game.blackRest = game.whiteRest
        } else {
            val timeGameWasClosed = System.currentTimeMillis() - game.systemMillis
            if (timeGameWasClosed > game.whiteRest && timeGameWasClosed > game.blackRest) {
                resetGame(game)
            }
        }
        deleteAllGame()

        return game
    }


    override suspend fun resetGame(game: Game?){
        game?.let {
            it.systemMillis = 0L
            it.isWhiteFirst = true
            it.isFirstPlayerThinking = true
            it.isPaused = false
            it.isGameFinished = false

            it.whiteRest = (it.min * 60 + it.sec) * 1000L
            it.blackRest = it.whiteRest
        }
    }


    override suspend fun getGameById(gameId: Int): Game? {
        return daoDB.getGameById(gameId)
    }


    override suspend fun insertIfNotEmptyTime(game: Game?) {
        game?.let {
            if (it.blackRest != 0L && it.whiteRest != 0L && !it.isGameFinished) {
                Timber.d("qwer saveGame: %s", game)
                daoDB.insertGame(it)
            } else {
                deleteAllGame()
            }
        }
    }


    override suspend fun deleteGame(gameId: Int): Int {
        return daoDB.deleteGame(gameId)
    }


    override suspend fun deleteAllGame(): Int {
        return daoDB.deleteAllGame()
    }

}