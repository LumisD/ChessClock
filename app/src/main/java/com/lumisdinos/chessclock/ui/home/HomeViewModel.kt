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
    val restTimeBottom: LiveData<Long> = logicRepo.restTimeBottom
    val restTimeTop: LiveData<Long> = logicRepo.restTimeTop
    var isBottomPressedFirst: LiveData<Boolean> = logicRepo.isBottomPressedFirst
    val topButtonBG: LiveData<String> = logicRepo.topButtonBG
    val bottomButtonBG: LiveData<String> = logicRepo.bottomButtonBG

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


    fun clickOnTopButtonView() {
        logicRepo.clickMoveButton(false)
    }


    fun clickOnBottomButtonView() {
        logicRepo.clickMoveButton(true)
    }


    fun clickOnPause() {
        logicRepo.clickPause()
    }


    fun clickShowMenu() {
        _openDrawer.postValue(Event(true))
    }

}