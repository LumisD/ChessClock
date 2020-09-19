package com.lumisdinos.chessclock.di

import android.content.Context
import com.lumisdinos.chessclock.ChessClockApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        AndroidSupportInjectionModule::class,
        HomeModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<ChessClockApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}