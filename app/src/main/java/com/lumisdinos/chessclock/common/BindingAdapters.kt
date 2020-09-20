package com.lumisdinos.chessclock.common

import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.lumisdinos.chessclock.R
import com.lumisdinos.chessclock.common.utils.InputFilterMinMax
import com.lumisdinos.chessclock.common.utils.convertLongToDateString
import com.lumisdinos.chessclock.data.AppConfig
import com.lumisdinos.chessclock.data.Constants.DATE
import com.lumisdinos.chessclock.common.utils.floatToStr
import com.lumisdinos.chessclock.common.utils.intToStr
import com.lumisdinos.chessclock.data.Constants.PERCENT_CHANGE_MONTH
import com.lumisdinos.chessclock.data.model.Game
import com.lumisdinos.tabletransform.common.extension.toNotNull
import timber.log.Timber
import java.lang.StringBuilder


object BindingAdapters {

    @JvmStatic
    @BindingAdapter("timeFromGame")
    fun convertTimeFromGame(textView: TextView, gameLive: LiveData<Game?>) {
        Timber.d("qwer timeFromGame")
        val game = gameLive?.value
        if (game == null) {
            textView.text = textView.context.getString(R.string.time_control_not_set)
            return
        }

        if (game.min == 0 && game.sec == 0 && game.inc == 0) {
            textView.text = textView.context.getString(R.string.time_control_not_set)
            return
        }
        Timber.d("qwer timeFromGame game: %s", game)
        val builder = StringBuilder()
        builder.append(game.min)
        if (game.sec != 0) {
            builder.append(".").append(game.sec)
        }
        if (game.inc != 0) {
            builder.append(" +").append(game.inc)
        }

        textView.text = builder.toString()
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
