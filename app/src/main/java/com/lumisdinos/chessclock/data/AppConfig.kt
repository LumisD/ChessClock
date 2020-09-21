package com.lumisdinos.chessclock.data

object AppConfig {

    var min = 0//15
    var sec = 10//0
    var inc = 3//10


    var previousClickTimeMillis = 0L

    //var isFirstLaunch = true
    var fragmentHeight = 2340
    var fragmentWidth = 1080
    var density = 3F
    var orientation = 1

    var decimalPrecisionForView = 1//later it will be changeable from settings fragment

    val configurationChangeDelay = 400L
    val shortDelay = 200L
    val longDelay = 1000L
    val veryLongDelay = 1500L

    var currentDateFormat = "dd/mm/yyy HH:mm:ss"

}