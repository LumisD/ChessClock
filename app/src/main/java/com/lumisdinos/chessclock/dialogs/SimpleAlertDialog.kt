package com.lumisdinos.chessclock.dialogs

import android.app.AlertDialog
import android.content.Context
import android.text.InputFilter
import android.view.LayoutInflater
import android.widget.*
import com.lumisdinos.chessclock.R
import com.lumisdinos.chessclock.common.utils.InputFilterMinMax
import com.lumisdinos.chessclock.common.utils.strToInt

fun getAlertDialogFull(
    context: Context,
    action: String,
    additional: String,
    listener: DialogListener,
    title: String,
    message: String,
    btnPositive: String,
    btnNegative: String,
    btnNeutral: String
): AlertDialog {

    val builder = AlertDialog.Builder(context)
    with(builder)
    {
        setTitle(title)
        setMessage(message)
        if (!btnPositive.isEmpty()) {
            setPositiveButton(btnPositive) { _, id -> listener.onPositiveDialogClick(listOf(action, additional)) }
        }
        if (!btnNegative.isEmpty()) {
            setNegativeButton(btnNegative) { _, id -> listener.onNegativeDialogClick(listOf(action)) }
        }
        if (!btnNeutral.isEmpty()) {
            setNeutralButton(btnNeutral) { _, id -> listener.onNeutralDialogClick(listOf(action)) }
        }
    }
    val dialog = builder.create()
    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)
    return dialog
}

fun getAlertDialog(
    context: Context,
    action: String,
    listener: DialogListener,
    title: String,
    message: String,
    btnPositive: String,
    btnNeutral: String
): AlertDialog {
    return getAlertDialogFull(context, action, "", listener, title, message, btnPositive, "", btnNeutral)
}

fun getAlertDialog(
    context: Context,
    action: String,
    additional: String,
    listener: DialogListener,
    title: String,
    message: String,
    btnPositive: String,
    btnNeutral: String
): AlertDialog {
    return getAlertDialogFull(context, action, additional, listener, title, message, btnPositive, "", btnNeutral)
}

fun getAlertDialog(
    context: Context,
    action: String,
    listener: DialogListener,
    title: String,
    message: String,
    btnPositive: String
): AlertDialog {
    return getAlertDialogFull(context, action, "", listener, title, message, btnPositive, "", "")
}


fun alertDialogToSetCustomTime(
    context: Context,
    inflater: LayoutInflater,
    action: String,
    listener: DialogListener,
    title: String,
    message: String,
    btnPositive: String,
    btnNeutral: String
): AlertDialog {

    val view = inflater.inflate(R.layout.alert_dialog_set_time, null)
    val minEt = view.findViewById<EditText>(R.id.minEt)
    val secEt = view.findViewById<EditText>(R.id.secEt)
    val incEt = view.findViewById<EditText>(R.id.incEt)
    secEt.filters = arrayOf<InputFilter>(InputFilterMinMax(0.toFloat(), 59.toFloat()))

    val builder = AlertDialog.Builder(context)
    with(builder)
    {
        setTitle(title)
        setMessage(message)
        setView(view)
        setPositiveButton(btnPositive) { _, id ->
            val min = strToInt(minEt.text.toString())
            val sec = strToInt(secEt.text.toString())
            val inc = strToInt(incEt.text.toString())
            val timeControl = "$min, $sec, $inc"
            listener.onPositiveDialogClick(
                listOf(action, timeControl)
            )
        }

        setNeutralButton(btnNeutral) { _, id -> listener.onNeutralDialogClick(listOf(action)) }
    }
    val dialog = builder.create()
    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)
    return dialog
}
