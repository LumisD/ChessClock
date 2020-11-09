package com.lumisdinos.chessclock.ui.home

import com.lumisdinos.chessclock.common.Event
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lumisdinos.chessclock.data.model.GameState
import com.lumisdinos.chessclock.data.repository.ChessClockLogicRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val logicRepo: ChessClockLogicRepository
) : ViewModel() {

    val gameState: LiveData<GameState> = logicRepo.gameStateLive

    init {
        logicRepo.initGame()
    }


    fun getGame() {
        logicRepo.createGame()
    }


    fun saveGame() {
        logicRepo.saveGame()
    }


    fun setChosenTimeControl(timeCtrl: String) {
        logicRepo.setChosenTimeControl(timeCtrl)
    }


    fun clickOnTopButtonView() {
        logicRepo.clickMoveButton(false)
    }


    fun clickOnBottomButtonView() {
        logicRepo.clickMoveButton(true)
    }


    fun clickOnPause() {
        logicRepo.clickPause()
    }

}