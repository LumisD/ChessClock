package com.lumisdinos.chessclock.common.utils

import com.lumisdinos.chessclock.data.AppConfig.longDelay
import com.lumisdinos.chessclock.data.AppConfig.previousClickTimeMillis
import com.lumisdinos.chessclock.data.AppConfig.shortDelay
import timber.log.Timber
import java.lang.NumberFormatException


fun strToInt(string: String, default: Int = 0): Int {
    return try {
        string.toInt()
    } catch (nfe: NumberFormatException) {
        Timber.d("qwer strToInt NumberFormatException: %s", nfe.message)
        default
    }
}


fun isClickedSingle(): Boolean {
    return isAlreadyClick(longDelay)
}


fun isClickedShort(): Boolean {
    return isAlreadyClick(shortDelay)
}


fun isAlreadyClick(time: Long): Boolean {
    val currentTimeMillis = System.currentTimeMillis()
    if (currentTimeMillis >= previousClickTimeMillis + time) {
        previousClickTimeMillis = currentTimeMillis
        return false
    } else {
        return true
    }
}
