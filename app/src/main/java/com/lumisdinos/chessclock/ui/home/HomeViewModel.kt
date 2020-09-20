package com.lumisdinos.chessclock.ui.home

import com.lumisdinos.chessclock.common.Event
import android.os.CountDownTimer
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

    private val _timeExpired = MutableLiveData<Event<String>>()
    val timeExpired: LiveData<Event<String>> = _timeExpired

    val gameLive = MutableLiveData<Game?>()
    var game: Game? = null

    val timeControlLive = MutableLiveData<String>()
    val changedToPauseIconLive = MutableLiveData<Boolean>()

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
                    game?.let { it.isFirstPlayerMoving = false }//handleNewMove inverses it
                }
            }
            game?.let {
                if (it.isPaused) {

                    var blackIsMoving = false
                    if (!it.isFirstPlayerMoving && it.isWhiteFirst) {
                        blackIsMoving = true
                    } else if (it.isFirstPlayerMoving && !it.isWhiteFirst) {
                        blackIsMoving = true
                    }

                    if (blackIsMoving) {
                        //only proceed if before pause it was black moving
                        clickOnPause()
                        return@launch
                    }
                } else {
                    handleNewMove()
                }

            }


        }
    }


    fun clickOnWhiteButtonView() {
        Timber.d("qwer clickOnWhiteButtonView")
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                if (game == null) return@withContext
                if (startFirstMove(true)) {
                    game?.let { it.isFirstPlayerMoving = false }//handleNewMove inverses it
                }
            }

            game?.let {
                if (it.isPaused) {

                    var whiteIsMoving = false
                    if (it.isFirstPlayerMoving && it.isWhiteFirst) {
                        whiteIsMoving = true
                    } else if (!it.isFirstPlayerMoving && !it.isWhiteFirst) {
                        whiteIsMoving = true
                    }

                    if (whiteIsMoving) {
                        //only proceed if before pause it was white moving
                        clickOnPause()
                        return@launch
                    }
                } else {
                    handleNewMove()
                }

            }


        }

    }


    fun clickOnPause() {
        game?.let {
            if (it.isPaused) {
                Timber.d("qwer clickOnPause if (it.isPaused)")
                //start again
                it.isPaused = false
                startTimer()
                //add paused time
                val pausedTime = System.currentTimeMillis() - it.pausedStartMillis
                it.pausedMillis += pausedTime
                it.pausedStartMillis = 0L
                changedToPauseIconLive.postValue(true)
            } else {
                Timber.d("qwer clickOnPause if NOT it.isPaused)")
                //pause time
                timer?.let {
                    Timber.d("qwer clickOnPause timer.cancell")
                    it.cancel()
                }
                timer = null
                it.isPaused = true
                it.pausedStartMillis = System.currentTimeMillis()
                changedToPauseIconLive.postValue(false)
            }
        }
    }


    fun clickShowMenu() {
        Timber.d("qwer clickShowMenu")

    }


    private fun handleNewMove() {
        Timber.d("qwer handleNewMove")
        game?.let {
            it.systemMillis = System.currentTimeMillis()
            it.isFirstPlayerMoving = !it.isFirstPlayerMoving
            Timber.d("qwer handleNewMove isFirstPlayerMoving: %s", it.isFirstPlayerMoving)
        }
        timer?.let { it.cancel() }
        timer = null
        startTimer()
    }


    private fun startTimer() {
//        timer = fixedRateTimer(period = 100L) {
//            Timber.d("qwer startTimer")
//        }
        val millFuture = if (game!!.isFirstPlayerMoving) game!!.whiteRest else game!!.blackRest
        Timber.d("qwer startTimer millFuture: %s", millFuture)

        timer = object : CountDownTimer(millFuture, 100) {
            override fun onTick(millisUntilFinished: Long) {
                Timber.d("qwer startTimer onTick")
                game?.let {
                    if (it.isFirstPlayerMoving) {
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
                        "qwer startTimer onFinish isFirstPlayerMoving: %s, isWhiteFirst: %s",
                        it.isFirstPlayerMoving,
                        it.isWhiteFirst
                    )
                    if (it.isFirstPlayerMoving) {
                        sideWhichExpired = "white"
                    } else if (!it.isFirstPlayerMoving) {
                        sideWhichExpired = "black"
                    }
                    //set to 0 rest time
                    if (it.isFirstPlayerMoving) it.whiteRest = 0L else it.blackRest = 0L
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