package com.lumisdinos.chessclock.common

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.lumisdinos.chessclock.R
import com.lumisdinos.chessclock.common.utils.convertLongToDateString
import com.lumisdinos.chessclock.common.utils.floatToStr
import com.lumisdinos.chessclock.common.utils.intToStr
import com.lumisdinos.chessclock.common.utils.strToInt
import com.lumisdinos.chessclock.data.AppConfig
import com.lumisdinos.chessclock.data.Constants.CLOCK_PLAYER_TIME_FORMAT
import com.lumisdinos.chessclock.data.Constants.CLOCK_PLAYER_TIME_FORMAT_DEC
import com.lumisdinos.chessclock.data.Constants.CLOCK_PLAYER_TIME_FORMAT_LESS_10M
import com.lumisdinos.chessclock.data.Constants.DATE
import com.lumisdinos.chessclock.data.Constants.PERCENT_CHANGE_MONTH
import com.lumisdinos.tabletransform.common.extension.toNotNull
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


object BindingAdapters {

    @JvmStatic
    @BindingAdapter("timeControlFromGame")
    fun convertTimeControlFromGame(textView: TextView, timeControlLive: LiveData<String>) {
        //Timber.d("qwer timeFromGame")
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
        //Timber.d("qwer timeFromGame game: %s", game)
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
            Timber.d("qwer changePausedIcon if (changedToPause)")
            imageButton.setImageResource(R.drawable.ic_pause_black_24dp)
        } else {
            Timber.d("qwer changePausedIcon if NOT changedToPause)")
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

        //Timber.d("qwer restClockTime restTime: %s", restTime)
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
        //0 - starting(no pressed); 1 - is pressed; 2 - is paused; 3 - waiting(no pressed, but another color is pressed)
        val buttonBG = whiteButtonBGLive.value ?: 0
        val isWhiteFirst = isWhiteFirstLive.value ?: true//if isWhiteFirst WhiteButton is white else WhiteButton is black
        //Timber.d("qwer whiteButtonsBG buttonBG: %s", buttonBG)
        val context = imageView.context
        if (buttonBG == 2) {
            if (isWhiteFirst) {
                imageView.setImageResource(R.drawable.ic_pause_black_24dp)
            } else {
                imageView.setImageResource(R.drawable.ic_pause_white_24dp)
            }
        } else {
            imageView.setImageResource(0)
        }

        if (isWhiteFirst) {//WhiteButton is white
            if (buttonBG == 0) {
                imageView.background = context.getDrawable(R.drawable.border_gray_button)//chess_paun_176x286
            } else if (buttonBG == 1) {
                imageView.background = context.getDrawable(R.drawable.border_white_pressed)
                imageView.setImageResource(R.drawable.chess_paun_176x286)
            } else if (buttonBG == 2) {
                imageView.background = context.getDrawable(R.drawable.border_white_paused)
            } else {
                imageView.background = context.getDrawable(R.drawable.border_white_waiting)
                imageView.setImageResource(0)
            }
        } else {//WhiteButton is black
            if (buttonBG == 0) {
                imageView.background = context.getDrawable(R.drawable.border_gray_button)
            } else if (buttonBG == 1) {
                imageView.background = context.getDrawable(R.drawable.border_black_pressed)
                imageView.setImageResource(R.drawable.chess_paun_176x286)
            } else if (buttonBG == 2) {
                imageView.background = context.getDrawable(R.drawable.border_black_paused)
            } else {
                imageView.background = context.getDrawable(R.drawable.border_black_waiting)
                imageView.setImageResource(0)
            }
        }

    }


    @JvmStatic
    @BindingAdapter(
        value = ["blackButtonsBG", "isWhiteFirst"],
        requireAll = true
    )
    fun setBlackButtonsBG(imageView: ImageView, blackButtonBGLive: LiveData<Int>, isWhiteFirstLive: LiveData<Boolean>) {
        //0 - starting(no pressed); 1 - is pressed; 2 - is paused; 3 - waiting(no pressed, but another color is pressed)
        val buttonBG = blackButtonBGLive.value ?: 0
        val isWhiteFirst = isWhiteFirstLive.value ?: true//if isWhiteFirst BlackButton is black else BlackButton is white
        val context = imageView.context
        //Timber.d("qwer setBlackButtonsBG buttonBG: %s", buttonBG)
        if (buttonBG == 2) {
            if (isWhiteFirst) {
                imageView.setImageResource(R.drawable.ic_pause_white_24dp)
            } else {
                imageView.setImageResource(R.drawable.ic_pause_black_24dp)
            }
        } else {
            imageView.setImageResource(0)
        }

        if (isWhiteFirst) {//BlackButton is black
            if (buttonBG == 0) {
                imageView.background = context.getDrawable(R.drawable.border_gray_button)
            } else if (buttonBG == 1) {
                imageView.background = context.getDrawable(R.drawable.border_black_pressed)
                imageView.setImageResource(R.drawable.chess_paun_176x286)
            } else if (buttonBG == 2) {
                imageView.background = context.getDrawable(R.drawable.border_black_paused)
            } else {
                imageView.background = context.getDrawable(R.drawable.border_black_waiting)
                imageView.setImageResource(0)
            }
        } else {//BlackButton is white
            if (buttonBG == 0) {
                imageView.background = context.getDrawable(R.drawable.border_gray_button)
            } else if (buttonBG == 1) {
                imageView.background = context.getDrawable(R.drawable.border_white_pressed)
                imageView.setImageResource(R.drawable.chess_paun_176x286)
            } else if (buttonBG == 2) {
                imageView.background = context.getDrawable(R.drawable.border_white_paused)
            } else {
                imageView.background = context.getDrawable(R.drawable.border_white_waiting)
                imageView.setImageResource(0)
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


    @JvmStatic
    @BindingAdapter("floatToStr")
    fun convertFloatToString(textView: TextView, value: Float?) {
        var result: String = "0"
        runCatching {
            result = floatToStr(value, AppConfig.decimalPrecisionForView, false)
        }.onSuccess {
            textView.text = result
        }.onFailure {
            textView.text = "0"
        }
    }

    @JvmStatic
    @BindingAdapter("intToStr")
    fun convertIntToString(view: TextView, value: Int?) {
        var result: String = "0"
        runCatching {
            result = intToStr(value)
        }.onSuccess {
            view.text = result
        }.onFailure {
            view.text = "0"
        }
    }

    @JvmStatic
    @BindingAdapter("isTxtFileTypeDate")
    fun isTxtFileTypeDate(view: View, value: String?) {
        if (DATE == value) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter("isTxtFileTypeNotDate")
    fun isTxtFileTypeNotDate(view: View, value: String?) {
        if (DATE != value) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }


    @JvmStatic
    @BindingAdapter("longToDataStr")
    fun convertLongToDataString(view: TextView, value: Long) {
        var result: String = ""
        runCatching {
            result = convertLongToDateString(value, "dd/MM/yy HH:mm:ss")
        }.onSuccess {
            view.text = result
        }.onFailure {
            view.text = ""
        }
    }


    @JvmStatic
    @BindingAdapter("intToBG")
    fun convertIntToBackground(view: View, value: Int) {
        val context = view.context
        if (value == 0) {
            view.setBackgroundColor(context.resources.getColor(R.color.table_cell_background))
        } else if (value == 1) {
            view.setBackgroundColor(context.resources.getColor(R.color.combined_background_1))
        } else {
            view.setBackgroundColor(context.resources.getColor(R.color.combined_background_2))
        }
    }


    @BindingAdapter(
        value = ["additional", "count"],
        requireAll = true
    )
    @JvmStatic
    fun changeFloatToBottomText(textView: TextView, additional: String, value: Float?) {
        var result = "0"
        runCatching {
            var resultFloat = value.toNotNull()

            if (additional.equals(PERCENT_CHANGE_MONTH)) {
                resultFloat *= 100
            }

            result = floatToStr(resultFloat, AppConfig.decimalPrecisionForView, false)

            if (additional.equals(PERCENT_CHANGE_MONTH)) {
                result = "$result%"
            }

        }.onSuccess {
            textView.text = result
        }.onFailure {
            textView.text = "0"
        }
    }

    @JvmStatic
    @BindingAdapter("intToTextColor")
    fun convertIntToTextColor(textView: TextView, value: Int) {
        val context = textView.context
        if (value == 0) {
            textView.setTextColor(context.resources.getColor(R.color.black_text))
        } else if (value == 1) {
            textView.setTextColor(context.resources.getColor(R.color.red_text))
        } else {
            textView.setTextColor(context.resources.getColor(R.color.green_text))
        }
    }


//    @JvmStatic
//    @BindingAdapter("restClockTime")
//    fun convertRestClockTime(textView: TextView, restTimeLive: LiveData<Long>) {
//        val restTime = restTimeLive.value ?: 0L
//        if (restTime == 0L) {
//            textView.text = "0.0"
//            return
//        }
//
////        val decSec = restTime % 100
////        val sec = restTime / 1000
////        val min = restTime / 1000 / 60
////        val hours = restTime / 1000 / 60 / 60
//
//        //900 000, hours: 0, min: 15, sec: 900
//
//        val hours = restTime / 1000 / 60 / 60
//        val min = restTime / 1000 / 60
//        val sec = restTime / 1000
//
//        val str = String.format("%02d min, %02d sec",
//            TimeUnit.MILLISECONDS.toMinutes(restTime),
//            TimeUnit.MILLISECONDS.toSeconds(restTime) -
//                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(restTime))
//        )
//        Timber.d("qwer restClockTime str: %s", str)
//
//        val time: Long = 1536259
//        val str2 =  SimpleDateFormat("mm:ss.S").format(Date(time))
//        Timber.d("qwer restClockTime str2: %s", str2)
//
//
//        val builder = StringBuilder()
//        if (hours > 0) {
//            builder.append(hours).append("h:")
//            if (min > 0) {
//                builder.append(min).append(":")
//            } else {
//                builder.append("00:")
//            }
//        } else {
//            //no hours
//            if (min > 0) {
//                builder.append(min).append(":")
//            } else {
//                builder.append("0:")
//            }
//        }
//
//        builder.append(sec)
//
//        //show decimal if rest only less than 20 sec
//        if (hours == 0L && min == 0L && sec < 20) {
//            val decSec = restTime % 100
//            builder.append(".").append(decSec)
//            Timber.d("qwer restClockTime decSec: %s", decSec)
//        }
//
//        Timber.d("qwer restClockTime restTime: %s, hours: %s, min: %s, sec: %s", restTime, hours, min, sec)
//        textView.text = builder.toString()
//    }


}
