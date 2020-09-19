package com.lumisdinos.chessclock.utils

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