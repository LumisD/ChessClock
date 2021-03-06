package com.lumisdinos.chessclock.common

import android.annotation.SuppressLint
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.lumisdinos.chessclock.R
import com.lumisdinos.chessclock.common.utils.strToInt
import com.lumisdinos.chessclock.data.Constants.BLACK_PAUSING_BG
import com.lumisdinos.chessclock.data.Constants.BLACK_THINKING_BG
import com.lumisdinos.chessclock.data.Constants.BLACK_WAITING_BG
import com.lumisdinos.chessclock.data.Constants.CLOCK_PLAYER_TIME_FORMAT
import com.lumisdinos.chessclock.data.Constants.CLOCK_PLAYER_TIME_FORMAT_DEC
import com.lumisdinos.chessclock.data.Constants.CLOCK_PLAYER_TIME_FORMAT_LESS_10M
import com.lumisdinos.chessclock.data.Constants.WHITE_PAUSING_BG
import com.lumisdinos.chessclock.data.Constants.WHITE_THINKING_BG
import com.lumisdinos.chessclock.data.Constants.WHITE_WAITING_BG
import java.text.SimpleDateFormat
import java.util.*


object BindingAdapters {

    @JvmStatic
    @BindingAdapter("timeControlFromGame")
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


    @JvmStatic
    @BindingAdapter("changePausedIcon")
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


    @JvmStatic
    @BindingAdapter("restClockTime")
    @SuppressLint("SimpleDateFormat")
    fun convertRestClockTime(textView: TextView, restTime: Long) {
        if (restTime == 0L) {
            textView.text = textView.context.getString(R.string.default_time)
            return
        }

        val str: String

        if (restTime < 20_000) {
            str = SimpleDateFormat(CLOCK_PLAYER_TIME_FORMAT_DEC).format(Date(restTime))
        } else if (restTime < 600_000) {
            str = SimpleDateFormat(CLOCK_PLAYER_TIME_FORMAT_LESS_10M).format(Date(restTime))
        } else {
            str = SimpleDateFormat(CLOCK_PLAYER_TIME_FORMAT).format(Date(restTime))
        }

        textView.text = str
    }


    @JvmStatic
    @BindingAdapter("buttonsBG")
    fun setButtonsBG(imageView: ImageView, buttonBG: String) {
        val context = imageView.context

        when (buttonBG) {
            WHITE_THINKING_BG -> {
                imageView.background = ContextCompat.getDrawable(context, R.drawable.border_white_thinking)
                imageView.setImageResource(R.drawable.chess_paun_176x286)
            }
            WHITE_WAITING_BG -> {
                imageView.background = ContextCompat.getDrawable(context, R.drawable.border_white_waiting)
                imageView.setImageResource(0)
            }
            WHITE_PAUSING_BG -> {
                imageView.background = ContextCompat.getDrawable(context, R.drawable.border_white_paused)
                imageView.setImageResource(R.drawable.ic_play_arrow_black_24dp)
            }
            BLACK_THINKING_BG -> {
                imageView.background = ContextCompat.getDrawable(context, R.drawable.border_black_thinking)
                imageView.setImageResource(R.drawable.chess_paun_176x286)
            }
            BLACK_WAITING_BG -> {
                imageView.background = ContextCompat.getDrawable(context, R.drawable.border_black_waiting)
                imageView.setImageResource(0)
            }
            BLACK_PAUSING_BG -> {
                imageView.background = ContextCompat.getDrawable(context, R.drawable.border_black_paused)
                imageView.setImageResource(R.drawable.ic_play_arrow_white_24dp)
            }
            else -> {//STARTING_BG
                imageView.background = ContextCompat.getDrawable(context, R.drawable.border_gray_button)
                imageView.setImageResource(0)
            }
        }
    }


    @JvmStatic
    @BindingAdapter("buttonTextColor")
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

}
