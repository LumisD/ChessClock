package com.lumisdinos.chessclock.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lumisdinos.chessclock.common.utils.strToInt
import com.lumisdinos.chessclock.data.AppConfig
import com.lumisdinos.chessclock.data.model.Game
import com.lumisdinos.chessclock.data.repository.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    val gameLive = MutableLiveData<Game?>()
    var game: Game? = null


    fun getGame() {
        Timber.d("qwer getGame")
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                game = gameRepository.getFirstGame()
                if (game == null) {
                    game = Game()
                    game!!.min = AppConfig.min
                    game!!.sec = AppConfig.sec
                    game!!.inc = AppConfig.inc
                }
                gameLive.postValue(game)
            }
        }

    }


    fun setTimeControl(timeControl: String) {
        //"15, 0, 10"
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val times = timeControl.split(",").map { it.trim() }
                Timber.d("qwer setTimeControl: |%s|", times)
                game?.min = strToInt(times[0])
                game?.sec = strToInt(times[1])
                game?.inc = strToInt(times[2])
                gameLive.postValue(game)
            }
        }

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