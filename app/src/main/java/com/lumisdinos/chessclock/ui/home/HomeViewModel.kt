package com.lumisdinos.chessclock.ui.home

import com.lumisdinos.chessclock.common.Event
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lumisdinos.chessclock.common.utils.strToInt
import com.lumisdinos.chessclock.data.AppConfig
import com.lumisdinos.chessclock.data.Constants._15_10
import com.lumisdinos.chessclock.data.model.Game
import com.lumisdinos.chessclock.data.repository.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.StringBuilder
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _timeExpired = MutableLiveData<Event<String>>()
    val timeExpired: LiveData<Event<String>> = _timeExpired

    val gameLive = MutableLiveData<Game?>()
    var game: Game? = null

    val timeControlLive = MutableLiveData<String>()

    var timer: CountDownTimer? = null


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
                game?.let {
                    timeControlLive.postValue("${it.min}, ${it.sec}, ${it.inc}")
                }
            }
        }

    }


    fun setChosenTimeControl(timeCtrl: String) {
        //"15, 0, 10"
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val times = timeCtrl.split(",").map { it.trim() }
                Timber.d("qwer setChosenTimeControl: |%s|", times)
                game?.let {
                    it.min = strToInt(times[0])
                    it.sec = strToInt(times[1])
                    it.inc = strToInt(times[2])
                    timeControlLive.postValue("${it.min}, ${it.sec}, ${it.inc}")
                }

            }
        }

    }


    fun clickOnBlackButtonView() {
        Timber.d("qwer clickOnBlackButtonView")
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                if (game == null) return@withContext
                if (startFirstMove(false)) {
                    game?.let { it.isWhMoving = false }//handleNewMove inverses it
                }
            }
            handleNewMove()
        }
    }


    fun clickOnWhiteButtonView() {
        Timber.d("qwer clickOnWhiteButtonView")
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                if (game == null) return@withContext
                if (startFirstMove(true)) {
                    game?.let { it.isWhMoving = true }//handleNewMove inverses it
                }
            }
            handleNewMove()
        }

    }


    fun clickOnPause() {
        Timber.d("qwer clickOnPause")
        timer?.let { it.cancel() }
        timer = null
        game?.let { it.isPaused = true }

    }


    fun clickShowMenu() {
        Timber.d("qwer clickShowMenu")

    }


    private fun handleNewMove() {
        Timber.d("qwer handleNewMove")
        game?.let {
            it.systemMillis = System.currentTimeMillis()
            it.isWhMoving = !it.isWhMoving
        }
        startTimer()
    }


    private fun startTimer() {
//        timer = fixedRateTimer(period = 100L) {
//            Timber.d("qwer startTimer")
//        }
        val millFuture = if (game!!.isWhMoving) game!!.whiteRest else game!!.blackRest
        Timber.d("qwer startTimer millFuture: %s", millFuture)

        timer = object : CountDownTimer(millFuture, 100) {
            override fun onTick(millisUntilFinished: Long) {
                Timber.d("qwer startTimer onTick")
                game?.let {
                    if (it.isWhMoving) {
                        it.whiteRest -= 100L
                    } else {
                        it.blackRest -= 100L
                    }
                    //refresh
                    timeControlLive.postValue("${it.min}, ${it.sec}, ${it.inc}")
                }

            }

            override fun onFinish() {
                Timber.d("qwer startTimer onFinish")
                var sideWhichExpired = "white"

                game?.let {
                    //define which side expired: who started
                    Timber.d(
                        "qwer startTimer onFinish isWhMoving: %s, isWhiteFirst: %s",
                        it.isWhMoving,
                        it.isWhiteFirst
                    )
                    if (it.isWhMoving && it.isWhiteFirst) {
                        sideWhichExpired = "white"
                    } else if (!it.isWhMoving && it.isWhiteFirst) {
                        sideWhichExpired = "black"
                    } else if (!it.isWhMoving && !it.isWhiteFirst) {
                        sideWhichExpired = "white"
                    } else if (it.isWhMoving && !it.isWhiteFirst) {
                        sideWhichExpired = "black"
                    }
                    //set to 0 rest time
                    if (it.isWhMoving) it.whiteRest = 0L else it.blackRest = 0L
                    //refresh
                    timeControlLive.postValue("${it.min}, ${it.sec}, ${it.inc}")
                }

                _timeExpired.postValue(Event(sideWhichExpired))
            }
        }
        timer!!.start()
        //timer!!.cancel()
    }


    private fun startFirstMove(isWhiteFirst: Boolean): Boolean {
        game?.let {
            if (it.systemMillis == 0L) {
                Timber.d("qwer startFirstMove")
                //the game is not started yet
                it.systemMillis = System.currentTimeMillis()
                it.whiteRest = (it.min * 60 + it.sec) * 1000L
                it.blackRest = it.whiteRest
                it.isWhiteFirst = isWhiteFirst
                return true
            }
        }

        return false
    }


}