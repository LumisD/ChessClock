package com.lumisdinos.chessclock.data.repository

import androidx.lifecycle.MutableLiveData
import com.lumisdinos.chessclock.common.Event

interface ChessClockLogicRepository {

    var timeControl: MutableLiveData<String>
    var changedToPauseIcon: MutableLiveData<Boolean>
    var restTimeBottom: MutableLiveData<Long>
    var restTimeTop: MutableLiveData<Long>
    var isBottomPressedFirst: MutableLiveData<Boolean>
    var topButtonBG: MutableLiveData<String>
    var bottomButtonBG: MutableLiveData<String>

    var timeExpired: MutableLiveData<Event<String>>
    var moveSound: MutableLiveData<Event<Boolean>>

    fun createGame()

    fun saveGame()

    fun setChosenTimeControl(timeCtrl: String)

    fun clickMoveButton(isBottomPressed: Boolean)

    fun clickPause()
}