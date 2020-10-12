package com.lumisdinos.tabletransform.common.extension

import timber.log.Timber
import java.lang.NumberFormatException

@JvmName("postSuccessResult")
fun String.stringToInt(default: Int = 0): Int {
    return try {
        this.toInt()
    } catch (nfe: NumberFormatException) {
        default
    }
}