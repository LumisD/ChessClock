package com.lumisdinos.chessclock.common

import android.annotation.SuppressLint
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.lumisdinos.chessclock.R
import com.lumisdinos.chessclock.common.utils.strToInt
import com.lumisdinos.chessclock.data.Constants.CLOCK_PLAYER_TIME_FORMAT
import com.lumisdinos.chessclock.data.Constants.CLOCK_PLAYER_TIME_FORMAT_DEC
import com.lumisdinos.chessclock.data.Constants.CLOCK_PLAYER_TIME_FORMAT_LESS_10M
import java.text.SimpleDateFormat
import java.util.*


object BindingAdapters {

    @JvmStatic
    @BindingAdapter("timeControlFromGame")
    fun convertTimeControlFromGame(textView: TextView, timeControlLive: LiveData<String>) {
        val timeControl = timeControlLive?.value
        if (timeControl.isNullOrEmpty()) {
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
        changedToPauseIconLive: LiveData<Boolean>
    ) {
        val changedToPause = changedToPauseIconLive.value ?: true
        if (changedToPause) {
            imageButton.setImageResource(R.drawable.ic_pause_black_24dp)
        } else {
            imageButton.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        }
    }


    @JvmStatic
    @BindingAdapter("restClockTime")
    @SuppressLint("SimpleDateFormat")
    fun convertRestClockTime(textView: TextView, restTimeLive: LiveData<Long>) {
        val restTime = restTimeLive.value ?: 0L
        if (restTime == 0L) {
            textView.text = "0:00.0"
            return
        }

        val str: String

        if (restTime < 20_000) {
            str = SimpleDateFormat(CLOCK_PLAYER_TIME_FORMAT_DEC).format(Date(restTime))//m:ss.S
        } else if (restTime < 600_000) {
            str = SimpleDateFormat(CLOCK_PLAYER_TIME_FORMAT_LESS_10M).format(Date(restTime))//m:ss
        } else {
            str = SimpleDateFormat(CLOCK_PLAYER_TIME_FORMAT).format(Date(restTime))//mm:ss
        }

        textView.text = str
    }


    @JvmStatic
    @BindingAdapter(
        value = ["whiteButtonsBG", "isWhiteFirst"],
        requireAll = true
    )
    fun setWhiteButtonsBG(imageView: ImageView, whiteButtonBGLive: LiveData<Int>, isWhiteFirstLive: LiveData<Boolean>) {
        //0 - starting(no pressed); 1 - is not thinking; 2 - is paused; 3 - thinking
        val buttonBG = whiteButtonBGLive.value ?: 0
        val isWhiteFirst = isWhiteFirstLive.value ?: true//if isWhiteFirst WhiteButton is white else WhiteButton is black

        val context = imageView.context
        if (buttonBG == 2) {
            if (isWhiteFirst) {
                imageView.setImageResource(R.drawable.ic_play_arrow_black_24dp)
            } else {
                imageView.setImageResource(R.drawable.ic_play_arrow_white_24dp)
            }
        } else {
            imageView.setImageResource(0)
        }

        if (isWhiteFirst) {//WhiteButton is white
            if (buttonBG == 0) {
                imageView.background = context.getDrawable(R.drawable.border_gray_button)
            } else if (buttonBG == 1) {
                imageView.background = context.getDrawable(R.drawable.border_white_not_thinking)
                imageView.setImageResource(0)
            } else if (buttonBG == 2) {
                imageView.background = context.getDrawable(R.drawable.border_white_paused)
            } else {
                imageView.background = context.getDrawable(R.drawable.border_white_thinking)
                imageView.setImageResource(R.drawable.chess_paun_176x286)
            }
        } else {//WhiteButton is black
            if (buttonBG == 0) {
                imageView.background = context.getDrawable(R.drawable.border_gray_button)
            } else if (buttonBG == 1) {
                imageView.background = context.getDrawable(R.drawable.border_black_not_thinking)
                imageView.setImageResource(0)
            } else if (buttonBG == 2) {
                imageView.background = context.getDrawable(R.drawable.border_black_paused)
            } else {
                imageView.background = context.getDrawable(R.drawable.border_black_thinking)
                imageView.setImageResource(R.drawable.chess_paun_176x286)
            }
        }

    }


    @JvmStatic
    @BindingAdapter(
        value = ["blackButtonsBG", "isWhiteFirst"],
        requireAll = true
    )
    fun setBlackButtonsBG(imageView: ImageView, blackButtonBGLive: LiveData<Int>, isWhiteFirstLive: LiveData<Boolean>) {
        //0 - starting(no pressed); 1 - is not thinking; 2 - is paused; 3 - thinking
        val buttonBG = blackButtonBGLive.value ?: 0
        val isWhiteFirst = isWhiteFirstLive.value ?: true//if isWhiteFirst BlackButton is black else BlackButton is white
        val context = imageView.context

        if (buttonBG == 2) {
            if (isWhiteFirst) {
                imageView.setImageResource(R.drawable.ic_play_arrow_white_24dp)
            } else {
                imageView.setImageResource(R.drawable.ic_play_arrow_black_24dp)
            }
        } else {
            imageView.setImageResource(0)
        }

        if (isWhiteFirst) {//BlackButton is black
            if (buttonBG == 0) {
                imageView.background = context.getDrawable(R.drawable.border_gray_button)
            } else if (buttonBG == 1) {
                imageView.background = context.getDrawable(R.drawable.border_black_not_thinking)
                imageView.setImageResource(0)
            } else if (buttonBG == 2) {
                imageView.background = context.getDrawable(R.drawable.border_black_paused)
            } else {
                imageView.background = context.getDrawable(R.drawable.border_black_thinking)
                imageView.setImageResource(R.drawable.chess_paun_176x286)
            }
        } else {//BlackButton is white
            if (buttonBG == 0) {
                imageView.background = context.getDrawable(R.drawable.border_gray_button)
            } else if (buttonBG == 1) {
                imageView.background = context.getDrawable(R.drawable.border_white_not_thinking)
                imageView.setImageResource(0)
            } else if (buttonBG == 2) {
                imageView.background = context.getDrawable(R.drawable.border_white_paused)
            } else {
                imageView.background = context.getDrawable(R.drawable.border_white_thinking)
                imageView.setImageResource(R.drawable.chess_paun_176x286)
            }
        }
    }


    @JvmStatic
    @BindingAdapter("buttonTextColor")
    fun convertButtonTextColor(textView: TextView, isWhiteFirstLive: LiveData<Boolean>) {
        val isWhiteFirst = isWhiteFirstLive.value ?: true//if isWhiteFirst BlackButton is black else BlackButton is white
        val tag = textView.tag
        val context = textView.context
        if (isWhiteFirst) {
            if (tag == "white") {//whiteButton plays white
                textView.setTextColor(context.resources.getColor(R.color.colorBlack))
            } else {
                textView.setTextColor(context.resources.getColor(R.color.colorWhite))
            }

        } else {
            if (tag == "white") {//whiteButton plays black
                textView.setTextColor(context.resources.getColor(R.color.colorWhite))
            } else {
                textView.setTextColor(context.resources.getColor(R.color.colorBlack))
            }
        }
    }

}
