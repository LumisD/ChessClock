package com.lumisdinos.chessclock.common.utils

import timber.log.Timber
import java.lang.Exception
import java.lang.NumberFormatException
import java.math.RoundingMode


fun strToInt(string: String, default: Int = 0): Int {
    return try {
        string.toInt()
    } catch (nfe: NumberFormatException) {
        Timber.d("qwer strToInt NumberFormatException: %s", nfe.message)
        default
    }
}


fun intToStr(int: Int?): String {
    return try {
        int.toString()
    } catch (e: Exception) {
        Timber.d("qwer strToInt Exception: %s, int: |%s|", e.message, int)
        "0"
    }
}


fun floatToStr(float: Float?, precision: Int, showPointIfZero: Boolean): String {
    //example: float = 1.2345, precision = 2 -> result: 1.23
    //example: float = 1.0, showPointIfZero = false -> result: 1
    return try {
        //handle nulls
        if (float == null) return "0"

        if (float - float.toInt() == 0f) {
            //handle floats with 0 after dot: 1.0
            if (showPointIfZero) {
                return float.toString()
            } else {
                return intToStr(float.toInt())
            }
        } else {
            //handle floats with decimals after dot: 1.2; 1.23456
            val rounded = float.toBigDecimal().setScale(precision, RoundingMode.UP).toFloat().toString()
            return rounded
        }

    } catch (e: Exception) {
        Timber.d("qwer floatToStr Exception: %s, float: |%s|", e.message, float)
        "0"
    }
}
