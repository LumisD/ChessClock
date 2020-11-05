package com.lumisdinos.chessclock.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.lumisdinos.chessclock.data.Database
import com.lumisdinos.chessclock.data.repository.ChessClockLogicRepository
import com.lumisdinos.chessclock.data.repository.ChessClockLogicRepositoryImpl
import com.lumisdinos.chessclock.data.repository.GameRepository
import com.lumisdinos.chessclock.data.repository.GameRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [ApplicationModuleBinds::class])
object ApplicationModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideDataBase(context: Context): Database {
        return Room.databaseBuilder(
            context.applicationContext,
            Database::class.java,
            "Games.db"
        ).build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideDaoDB(db: Database) = db.daoDB()


    @JvmStatic
    @Singleton
    @Provides
    fun providePreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("games_preferences", MODE_PRIVATE)
    }

}


@Module
abstract class ApplicationModuleBinds {

    @Binds
    abstract fun bindGameRepositoryImpl(repository: GameRepositoryImpl): GameRepository

    @Binds
    abstract fun bindChessClockLogicRepositoryImpl(repository: ChessClockLogicRepositoryImpl): ChessClockLogicRepository

}