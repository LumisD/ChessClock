package com.lumisdinos.chessclock.common.extension

import android.view.View
import com.lumisdinos.chessclock.common.utils.OnSingleClickListener
import timber.log.Timber
import java.lang.NumberFormatException

fun View.setOnSingleClickListener(l: View.OnClickListener) {
    setOnClickListener(OnSingleClickListener(l))
}

fun View.setOnSingleClickListener(l: (View) -> Unit) {
    setOnClickListener(OnSingleClickListener(l))
}

//settingsButton.setOnSingleClickListener {
//    // navigation call here
//}