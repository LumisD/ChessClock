package com.lumisdinos.chessclock.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.lumisdinos.chessclock.data.Database
import com.lumisdinos.chessclock.data.repository.ChessClockLogicRepository
import com.lumisdinos.chessclock.data.ChessClockLogicRepositoryImpl
import com.lumisdinos.chessclock.data.repository.GameRepository
import com.lumisdinos.chessclock.data.GameRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module(includes = [ApplicationModuleBinds::class])
object ApplicationModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(
            context.applicationContext,
            Database::class.java,
            "Games.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDaoDB(db: Database) = db.daoDB()

    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("games_preferences", MODE_PRIVATE)
    }

}


@InstallIn(SingletonComponent::class)
@Module
abstract class ApplicationModuleBinds {

    @Binds
    abstract fun bindGameRepositoryImpl(repository: GameRepositoryImpl): GameRepository

    @Binds
    abstract fun bindChessClockLogicRepositoryImpl(repository: ChessClockLogicRepositoryImpl): ChessClockLogicRepository

}