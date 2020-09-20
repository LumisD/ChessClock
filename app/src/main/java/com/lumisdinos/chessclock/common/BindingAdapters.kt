package com.lumisdinos.chessclock.common

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.lumisdinos.chessclock.R
import com.lumisdinos.chessclock.common.utils.*
import com.lumisdinos.chessclock.data.AppConfig
import com.lumisdinos.chessclock.data.Constants.DATE
import com.lumisdinos.chessclock.data.Constants.PERCENT_CHANGE_MONTH
import com.lumisdinos.tabletransform.common.extension.toNotNull
import timber.log.Timber
import java.lang.StringBuilder


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
    fun convertChangePausedIcon(imageButton: ImageButton, changedToPauseIconLive: LiveData<Boolean>) {
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

}
