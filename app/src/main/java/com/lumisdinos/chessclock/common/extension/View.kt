package com.lumisdinos.chessclock.common.extension

import android.view.View
import com.lumisdinos.chessclock.common.utils.OnSingleClickListener

fun View.setOnSingleClickListener(l: View.OnClickListener) {
    setOnClickListener(OnSingleClickListener(l))
}

fun View.setOnSingleClickListener(l: (View) -> Unit) {
    setOnClickListener(OnSingleClickListener(l))
}