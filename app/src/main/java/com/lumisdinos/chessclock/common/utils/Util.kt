package com.lumisdinos.chessclock.common.utils

import android.annotation.SuppressLint
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.lumisdinos.chessclock.R
import com.lumisdinos.chessclock.data.AppConfig.longDelay
import com.lumisdinos.chessclock.data.AppConfig.previousClickTimeMillis
import com.lumisdinos.chessclock.data.AppConfig.shortDelay
import com.lumisdinos.chessclock.data.Constants
import timber.log.Timber
import java.lang.NumberFormatException
import java.text.SimpleDateFormat
import java.util.Date


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


fun setButtonsBG(imageView: ImageView, buttonBG: String) {
    val context = imageView.context

    when (buttonBG) {
        Constants.WHITE_THINKING_BG -> {
            imageView.background = ContextCompat.getDrawable(context, R.drawable.border_white_thinking)
            imageView.setImageResource(R.drawable.chess_paun_176x286)
        }
        Constants.WHITE_WAITING_BG -> {
            imageView.background = ContextCompat.getDrawable(context, R.drawable.border_white_waiting)
            imageView.setImageResource(0)
        }
        Constants.WHITE_PAUSING_BG -> {
            imageView.background = ContextCompat.getDrawable(context, R.drawable.border_white_paused)
            imageView.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        }
        Constants.BLACK_THINKING_BG -> {
            imageView.background = ContextCompat.getDrawable(context, R.drawable.border_black_thinking)
            imageView.setImageResource(R.drawable.chess_paun_176x286)
        }
        Constants.BLACK_WAITING_BG -> {
            imageView.background = ContextCompat.getDrawable(context, R.drawable.border_black_waiting)
            imageView.setImageResource(0)
        }
        Constants.BLACK_PAUSING_BG -> {
            imageView.background = ContextCompat.getDrawable(context, R.drawable.border_black_paused)
            imageView.setImageResource(R.drawable.ic_play_arrow_white_24dp)
        }
        else -> {//STARTING_BG
            imageView.background = ContextCompat.getDrawable(context, R.drawable.border_gray_button)
            imageView.setImageResource(0)
        }
    }
}


fun convertButtonTextColor(textView: TextView, isBottomFirst: Boolean) {
    val tag = textView.tag
    val context = textView.context
    if (isBottomFirst) {
        if (tag == "bottom") {
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
        }

    } else {
        if (tag == "bottom") {
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
        }
    }
}


fun convertTimeControlFromGame(textView: TextView, timeControl: String) {
    if (timeControl.isEmpty()) {
        textView.text = textView.context.getString(R.string.time_control_not_set)
        return
    }

    val times = timeControl.split(",").map { it.trim() }
    val min = strToInt(times[0])
    val sec = strToInt(times[1])
    val inc = strToInt(times[2])

    if (min == 0 && sec == 0 && inc == 0) {
        textView.text = textView.context.getString(R.string.time_control_not_set)
        return
    }

    val builder = StringBuilder()
    builder.append(min)
    if (sec != 0) {
        builder.append(".")
        if (sec < 10) {
            builder.append("0")
        }
        builder.append(sec)
    }
    if (inc != 0) {
        builder.append(" +").append(inc)
    }

    textView.text = builder.toString()
}


fun convertChangePausedIcon(
    imageButton: ImageButton,
    changedToPause: Boolean
) {
    if (changedToPause) {
        imageButton.setImageResource(R.drawable.ic_pause_black_24dp)
    } else {
        imageButton.setImageResource(R.drawable.ic_play_arrow_black_24dp)
    }
}


@SuppressLint("SimpleDateFormat")
fun convertRestClockTime(textView: TextView, restTime: Long) {
    if (restTime == 0L) {
        textView.text = textView.context.getString(R.string.default_time)
        return
    }

    val str: String

    str = if (restTime < 20_000) {
        SimpleDateFormat(Constants.CLOCK_PLAYER_TIME_FORMAT_DEC).format(Date(restTime))
    } else if (restTime < 600_000) {
        SimpleDateFormat(Constants.CLOCK_PLAYER_TIME_FORMAT_LESS_10M).format(Date(restTime))
    } else {
        SimpleDateFormat(Constants.CLOCK_PLAYER_TIME_FORMAT).format(Date(restTime))
    }

    textView.text = str
}
