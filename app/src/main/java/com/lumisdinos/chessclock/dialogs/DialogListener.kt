package com.lumisdinos.chessclock.dialogs

interface DialogListener {
    fun onPositiveDialogClick(result: List<String>)//action; string/s
    fun onNegativeDialogClick(result: List<String>)//action
    fun onNeutralDialogClick(result: List<String>)//action
}