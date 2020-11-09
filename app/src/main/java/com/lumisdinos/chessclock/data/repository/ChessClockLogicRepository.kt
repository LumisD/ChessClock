package com.lumisdinos.chessclock.data.repository

import androidx.lifecycle.MutableLiveData
import com.lumisdinos.chessclock.data.model.GameState

interface ChessClockLogicRepository {

    var gameStateLive: MutableLiveData<GameState>

    fun initGame()

    fun createGame()

    fun saveGame()

    fun setChosenTimeControl(timeCtrl: String)

    fun clickMoveButton(isBottomPressed: Boolean)

    fun clickPause()
}