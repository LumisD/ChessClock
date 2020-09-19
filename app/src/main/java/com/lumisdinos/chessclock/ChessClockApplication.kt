package com.lumisdinos.chessclock

import android.app.Application
import timber.log.Timber


open class ChessClockApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }


}