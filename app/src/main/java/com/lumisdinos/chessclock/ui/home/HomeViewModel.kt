package com.lumisdinos.chessclock.ui.home

import com.lumisdinos.chessclock.common.Event
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lumisdinos.chessclock.data.repository.ChessClockLogicRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val logicRepo: ChessClockLogicRepository
) : ViewModel() {

    private val _openDrawer = MutableLiveData<Event<Boolean>>()
    val openDrawer: LiveData<Event<Boolean>> = _openDrawer

    val timeControl: LiveData<String> = logicRepo.timeControl
    val changedToPauseIcon: LiveData<Boolean> = logicRepo.changedToPauseIcon
    val restTimeWhite: LiveData<Long> = logicRepo.restTimeWhite
    val restTimeBlack: LiveData<Long> = logicRepo.restTimeBlack
    var isWhiteFirst: LiveData<Boolean> = logicRepo.isWhiteFirst
    val blackButtonBG: LiveData<Int> = logicRepo.blackButtonBG
    val whiteButtonBG: LiveData<Int> = logicRepo.whiteButtonBG

    val moveSound: LiveData<Event<Boolean>> = logicRepo.moveSound
    val timeExpired: LiveData<Event<String>> = logicRepo.timeExpired


    fun getGame() {
        logicRepo.createGame()
    }


    fun saveGame() {
        logicRepo.saveGame()
    }


    fun setChosenTimeControl(timeCtrl: String) {
        logicRepo.setChosenTimeControl(timeCtrl)
    }


    fun clickOnBlackButtonView() {
        logicRepo.clickBlackButton()
    }


    fun clickOnWhiteButtonView() {
        logicRepo.clickWhiteButton()
    }


    fun clickOnPause() {
        logicRepo.clickPause()
    }


    fun clickShowMenu() {
        _openDrawer.postValue(Event(true))
    }

}