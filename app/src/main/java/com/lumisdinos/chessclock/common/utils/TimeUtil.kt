package com.lumisdinos.chessclock.common.utils

import android.annotation.SuppressLint
import com.lumisdinos.chessclock.data.Constants.DATE_FORMATS
import com.lumisdinos.chessclock.data.Constants.SHORT_SQLITE_DATE_FORMAT
import com.lumisdinos.chessclock.data.Constants.SQLITE_DATE_FORMAT
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long, dateFormat: String): String {
    try {
        return SimpleDateFormat(dateFormat)
            //return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
            .format(systemTime)
    } catch (e: java.lang.Exception) {
        return ""
    }
}


//iterate all date formats to define which is correct
fun defineDateFormats(dateStr: String): List<String> {
    val availableFormats = mutableListOf<String>()
    for (toFormat in DATE_FORMATS) {
        var date: Date? = null
        val formatter = SimpleDateFormat(toFormat, Locale.getDefault())
        try {
            //dateFormat.timeZone = TimeZone.getTimeZone("America/New_York")
            date = formatter.parse(dateStr)
        } catch (e: Exception) {
            Timber.d("qwer defineDateFormat wrong toFormat: %s, Exception: %s", toFormat, e.message)
        }
        if (date != null) {
            val backFormattedDateStr = formatter.format(date)
//            Timber.d(
//                "qwer defineDateFormat toFormat: %s, dateStrToFormat: %s, backFormattedDateStr: %s",
//                toFormat,
//                dateStr,
//                backFormattedDateStr
//            )
            if (dateStr.equals(backFormattedDateStr, ignoreCase = true)) {
                //Timber.d("qwer defineDateFormat -> return toFormat: %s", toFormat)
                availableFormats.add(toFormat)
                //return toFormat
            } else {
                //Timber.d("qwer defineDateFormat -> continue")
                continue
            }
        }
    }
    return availableFormats
}



//iterate all date formats to define which is correct
fun defineDateFormat(dateStr: String): String? {
    for (toFormat in DATE_FORMATS) {
        var date: Date? = null
        val formatter = SimpleDateFormat(toFormat, Locale.getDefault())
        try {
            //dateFormat.timeZone = TimeZone.getTimeZone("America/New_York")
            date = formatter.parse(dateStr)
        } catch (e: Exception) {
            Timber.d("qwer defineDateFormat wrong toFormat: %s, Exception: %s", toFormat, e.message)
        }
        if (date != null) {
            val backFormattedDateStr = formatter.format(date)
//            Timber.d(
//                "qwer defineDateFormat toFormat: %s, dateStrToFormat: %s, backFormattedDateStr: %s",
//                toFormat,
//                dateStr,
//                backFormattedDateStr
//            )
            if (dateStr.equals(backFormattedDateStr, ignoreCase = true)) {
                //Timber.d("qwer defineDateFormat -> return toFormat: %s", toFormat)
                return toFormat
            } else {
                //Timber.d("qwer defineDateFormat -> continue")
                continue
            }
        }
    }
    return null
}


//convert dateStr from one format to another
fun convertDateFormat(dateStr: String, fromFormat: String, toFormat: String): String? {
//    Timber.d(
//        "qwer convertDateFormat dateStr: %s,  fromFormat: %s, toFormat: %s",
//        dateStr,
//        fromFormat,
//        toFormat
//    )
    val parser = SimpleDateFormat(fromFormat, Locale.getDefault())
    val formatter = SimpleDateFormat(toFormat, Locale.getDefault())
    try {
        val date = parser.parse(dateStr) ?: return null
        val formattedDate = formatter.format(date)

        val backFormattedDateStr = parser.format(date)
//        Timber.d(
//            "qwer convertDateFormat formattedDate: %s, backFormattedDateStr: %s",
//            formattedDate,
//            backFormattedDateStr
//        )
        if (dateStr.equals(backFormattedDateStr, ignoreCase = true)) {
            //Timber.d("qwer convertDateFormat -> return formattedDate: %s", formattedDate)
            return formattedDate
        } else {
            //Timber.d("qwer convertDateFormat -> return null")
            return null
        }
    } catch (e: Exception) {
        Timber.d("qwer convertDateFormat Exception: %s", e.message)
        return null
    }
}


fun createDateInSqliteDateFormat(
    isShortFormat: Boolean,
    year: Int,
    month: Int,
    day: Int = 1,
    hours: Int = 0,
    min: Int = 0,
    secs: Int = 0,
    millSecs: Int = 0
): String? {
    //yyyy-MM-dd HH:mm:ss.SSS   SQLITE_DATE_FORMAT       2020-04-23 15:01:00.234
    //yyyy-MM-dd                SHORT_SQLITE_DATE_FORMAT
    val yearStr = intToStr(year)
    val monthStr = intToStr(month)
    val dayStr = intToStr(day)
    val hoursStr = intToStr(hours)
    val minStr = intToStr(min)
    val secsStr = intToStr(secs)
    val millSecsStr = intToStr(millSecs)
    Timber.d(
        "qwer createDateInSqliteDateFormat year: %s,  month: %s, day: %s, hours: %s, min: %s, secs: %s, millSecs: %s",
        year,
        month,
        day,
        hours,
        min,
        secs,
        millSecs
    )
    if (yearStr.length != 4) return null

    val builder = StringBuffer()
    builder.append(year).append("-")
        .append(if (monthStr.length == 1) "0" else "").append(monthStr)
        .append("-")
        .append(if (dayStr.length == 1) "0" else "").append(dayStr)
    if (!isShortFormat) {
        builder.append(" ")
            .append(if (hoursStr.length == 1) "0" else "").append(hoursStr)
            .append(":")
            .append(if (minStr.length == 1) "0" else "").append(minStr)
            .append(":")
            .append(if (secsStr.length == 1) "0" else "").append(secsStr)
            .append(".")
        if (millSecsStr.length == 1) {
            builder.append("00").append(millSecsStr)
        } else if (millSecsStr.length == 2) {
            builder.append("0").append(millSecsStr)
        } else {
            builder.append(millSecsStr)
        }
    }

    Timber.d("qwer createDateInSqliteDateFormat builder: %s", builder.toString())

    val sqliteDateFormat = if (isShortFormat) SHORT_SQLITE_DATE_FORMAT else SQLITE_DATE_FORMAT
    var date: Date? = null
    val formatter = SimpleDateFormat(sqliteDateFormat, Locale.getDefault())

    try {
        //dateFormat.timeZone = TimeZone.getTimeZone("America/New_York")
        date = formatter.parse(builder.toString())
    } catch (e: Exception) {
        Timber.d(
            "qwer createDateInSqliteDateFormat wrong sqliteDateFormat: %s, Exception: %s",
            sqliteDateFormat,
            e.message
        )
    }

    if (date != null) {
        Timber.d("qwer createDateInSqliteDateFormat -> return: %s ", builder.toString())
        return builder.toString()
    } else {
        Timber.d("qwer createDateInSqliteDateFormat -> return null: %s ", builder.toString())
        return null
    }
}


fun createDateInShortSqliteDateFormat(
    year: Int,
    month: Int,
    day: String = "01",
    hours: String = "00",
    min: String = "00",
    secs: String = "00",
    millSecs: String = "000"
): String? {
    //yyyy-MM-dd
    val yearStr = intToStr(year)
    val monthStr = intToStr(month)
    Timber.d(
        "qwer createDateInSqliteDateFormat year: %s,  month: %s, day: %s, hours: %s, min: %s, secs: %s, millSecs: %s",
        year,
        month,
        day,
        hours,
        min,
        secs,
        millSecs
    )
    if (yearStr.length != 4 || monthStr.length < 1) return null

    val builder = StringBuffer()
    builder.append(year).append("-")
        .append(if (monthStr.length == 1) "0" else "").append(monthStr)
        .append("-").append(day)
//        .append(" ").append(hours)
//        .append(":").append(min).append(":").append(secs).append(".").append(millSecs)

    val sqliteDateFormat = SHORT_SQLITE_DATE_FORMAT
    var date: Date? = null
    val formatter = SimpleDateFormat(sqliteDateFormat, Locale.getDefault())

    try {
        //dateFormat.timeZone = TimeZone.getTimeZone("America/New_York")
        date = formatter.parse(builder.toString())
    } catch (e: Exception) {
        Timber.d(
            "qwer createDateInSqliteDateFormat wrong sqliteDateFormat: %s, Exception: %s",
            sqliteDateFormat,
            e.message
        )
    }

    if (date != null) {
        Timber.d("qwer createDateInSqliteDateFormat -> return: %s ", builder.toString())
        return builder.toString()
    } else {
        Timber.d("qwer createDateInSqliteDateFormat -> return null: %s ", builder.toString())
        return null
    }
}