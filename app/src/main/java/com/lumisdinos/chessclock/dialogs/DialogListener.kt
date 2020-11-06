package com.lumisdinos.chessclock.dialogs

interface DialogListener {
    fun onPositiveDialogClick(result: List<String>)
    fun onNegativeDialogClick(result: List<String>)
    fun onNeutralDialogClick(result: List<String>)
}