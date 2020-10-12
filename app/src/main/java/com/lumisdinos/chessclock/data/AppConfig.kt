package com.lumisdinos.chessclock.data

object AppConfig {

    var min = 15
    var sec = 0
    var inc = 10

    var previousClickTimeMillis = 0L
    var decimalPrecisionForView = 1//later it will be changeable from settings fragment

    val shortDelay = 200L
    val longDelay = 1000L

}