package com.lumisdinos.chessclock.data.model

import com.lumisdinos.chessclock.common.Event

data class GameState(
    var timeControl: String = "",
    var changedToPauseIcon: Boolean = false,
    var restTimeBottom: Long = 0L,
    var restTimeTop: Long = 0L,
    var isBottomPressedFirst: Boolean = true,
    var topButtonBG: String = "",
    var bottomButtonBG: String = "",
    var timeExpired: Event<String> = Event(""),
    var moveSound: Event<Boolean> = Event(false)
)