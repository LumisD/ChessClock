package com.lumisdinos.chessclock.data.repository

import androidx.lifecycle.MutableLiveData
import com.lumisdinos.chessclock.common.Event

interface ChessClockLogicRepository {

    var timeControl: MutableLiveData<String>
    var changedToPauseIcon: MutableLiveData<Boolean>
    var restTimeWhite: MutableLiveData<Long>
    var restTimeBlack: MutableLiveData<Long>
    var isWhiteFirst: MutableLiveData<Boolean>
    var blackButtonBG: MutableLiveData<Int>
    var whiteButtonBG: MutableLiveData<Int>

    var timeExpired: MutableLiveData<Event<String>>
    var moveSound: MutableLiveData<Event<Boolean>>

    fun createGame()

    fun saveGame()

    fun setChosenTimeControl(timeCtrl: String)

    fun clickBlackButton()

    fun clickWhiteButton()

    fun clickPause()
}