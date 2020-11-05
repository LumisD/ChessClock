package com.lumisdinos.chessclock.data.repository

import androidx.lifecycle.MutableLiveData
import com.lumisdinos.chessclock.common.Event

interface ChessClockLogicRepository {

    var timeControl: MutableLiveData<String>
    var changedToPauseIcon: MutableLiveData<Boolean>
    var restTimeBottom: MutableLiveData<Long>
    var restTimeTop: MutableLiveData<Long>
    var isBottomFirst: MutableLiveData<Boolean>
    var topButtonBG: MutableLiveData<Int>
    var bottomButtonBG: MutableLiveData<Int>

    var timeExpired: MutableLiveData<Event<String>>
    var moveSound: MutableLiveData<Event<Boolean>>

    fun createGame()

    fun saveGame()

    fun setChosenTimeControl(timeCtrl: String)

    fun clickBottomButton()

    fun clickTopButton()

    fun clickPause()
}