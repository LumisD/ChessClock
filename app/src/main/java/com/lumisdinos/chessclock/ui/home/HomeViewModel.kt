package com.lumisdinos.chessclock.ui.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text


    fun doSom() {
        Timber.d("qwer doSom")
    }


    fun clickOnTopButtonView() {
        Timber.d("qwer clickOnTopButtonView")

    }


    fun clickOnBottomButtonView() {
        Timber.d("qwer clickOnBottomButtonView")

    }


    fun clickOnPause() {
        Timber.d("qwer clickOnPause")

    }


    fun clickShowMenu() {
        Timber.d("qwer clickShowMenu")

    }
}