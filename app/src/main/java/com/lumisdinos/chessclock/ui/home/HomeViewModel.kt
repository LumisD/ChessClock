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

    private val _openDrawer = MutableLiveData<Event<Boolean>>()
    val openDrawer: LiveData<Event<Boolean>> = _openDrawer

    var game: Game? = null

    val timeControlLive = MutableLiveData<String>()
    val changedToPauseIconLive = MutableLiveData<Boolean>()
    val restTimeWhiteLive = MutableLiveData<Long>()
    val restTimeBlackLive = MutableLiveData<Long>()
    var isWhiteFirstLive = MutableLiveData<Boolean>()//define was button was pressed first blackButtonView or blackButtonView
    val blackButtonBGLive = MutableLiveData<Int>()//0 - starting(no pressed); 1 - is pressed; 2 - is paused
    val whiteButtonBGLive = MutableLiveData<Int>()//0 - starting(no pressed); 1 - is pressed; 2 - is paused; 3 - waiting(no pressed, but another color is pressed)

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
                    game!!.whiteRest = (AppConfig.min * 60 + AppConfig.sec) * 1000L
                    game!!.blackRest = game!!.whiteRest
                } else {
                    game?.let {
                        val timeGameWasClosed = System.currentTimeMillis() - it.systemMillis
                        if (timeGameWasClosed > it.whiteRest && timeGameWasClosed > it.blackRest) {
                            resetGame()
                        }
                    }

                }
                gameRepository.deleteAllGame()

                game?.let {
                    Timber.d("qwer getGame: %s", game)

//                    systemMillis=1600739200634,
//                    min=0,
//                    sec=3,
//                    inc=1,
//                    whiteRest=2500,
//                    blackRest=1900,
//                    pausedMillis=774475,
//                    pausedStartMillis=1600739201700,
//
//                    isWhiteFirst=true,
//                    isFirstPlayerMoving=false,
//                    isPaused=true

                    //set live vars
                    restTimeWhiteLive.postValue(it.whiteRest)
                    restTimeBlackLive.postValue(it.blackRest)

                    if (it.isWhiteFirst) {//whiteButton goes first
                        if (it.isFirstPlayerMoving) {//whiteButton first AND white is moving
                            if (it.isPaused) {
                                whiteButtonBGLive.postValue(2)//paused
                            } else {
                                whiteButtonBGLive.postValue(1)//pressed
                            }
                            blackButtonBGLive.postValue(3)//waiting
                        } else {//whiteButton first AND black is moving
                            if (it.isPaused) {
                                blackButtonBGLive.postValue(2)//paused
                            } else {
                                blackButtonBGLive.postValue(1)//pressed
                            }
                            whiteButtonBGLive.postValue(3)//waiting
                        }
                    } else {//blackButton goes first
                        if (it.isFirstPlayerMoving) {//blackButton first AND black is moving
                            if (it.isPaused) {
                                blackButtonBGLive.postValue(2)//paused
                            } else {
                                blackButtonBGLive.postValue(1)//pressed
                            }
                            whiteButtonBGLive.postValue(3)//waiting
                        } else {//blackButton first AND white is moving
                            if (it.isPaused) {
                                whiteButtonBGLive.postValue(2)//paused
                            } else {
                                whiteButtonBGLive.postValue(1)//pressed
                            }
                            blackButtonBGLive.postValue(3)//waiting
                        }
                    }

                    isWhiteFirstLive.postValue(it.isWhiteFirst)

                    if (it.isPaused) {
                        changedToPauseIconLive.postValue(true)
                    } else {
                        changedToPauseIconLive.postValue(false)
                    }

                    timeControlLive.postValue("${it.min}, ${it.sec}, ${it.inc}")
                }//game?.let
                Timber.d("qwer getGame end of withContext")

            }

            Timber.d("qwer getGame just after end of withContext")
            game?.let {
                if (!it.isPaused && it.systemMillis > 0) {
                    startTimer()
                }
            }

        }

    }


    fun saveGame() {
        Timber.d("qwer saveGame")
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                game?.let {
                    if (it.blackRest != 0L && it.whiteRest != 0L) {
                        Timber.d("qwer saveGame: %s", game)
                        gameRepository.insertGame(it)
                    }
                }
            }
        }
    }


    fun setChosenTimeControl(timeCtrl: String) {
        //"15, 0, 10"
        CoroutineScope(Dispatchers.Main).launch {
            timer?.let { it.cancel() }
            timer = null
            withContext(Dispatchers.IO) {
                val times = timeCtrl.split(",").map { it.trim() }
                Timber.d("qwer setChosenTimeControl: |%s|", times)
                game?.let {
                    //set new time control
                    it.min = strToInt(times[0])
                    it.sec = strToInt(times[1])
                    it.inc = strToInt(times[2])

                    //reset game
                    resetGame()

                    timeControlLive.postValue("${it.min}, ${it.sec}, ${it.inc}")
                    restTimeWhiteLive.postValue(it.whiteRest)
                    restTimeBlackLive.postValue(it.blackRest)
                    whiteButtonBGLive.postValue(0)//starting(no pressed)
                    blackButtonBGLive.postValue(0)//starting(no pressed)
                    changedToPauseIconLive.postValue(false)
                }

            }
        }

    }


    fun clickShowMenu() {
        Timber.d("qwer clickShowMenu")
        _openDrawer.postValue(Event(true))
    }


    fun clickOnBlackButtonView() {
        Timber.d("qwer clickOnBlackButtonView")
        CoroutineScope(Dispatchers.Main).launch {
            var isTheFirstMove = false
            if (game == null) return@launch
            isTheFirstMove = startFirstMove(false)
            if (isTheFirstMove) return@launch

            game?.let {

                //define which clock is going
                var blackIsMoving = false
                if (it.isWhiteFirst && !it.isFirstPlayerMoving) {//white was first, but now not his move -> so, black is moving
                    blackIsMoving = true
                } else if (!it.isWhiteFirst && it.isFirstPlayerMoving) {//white was not first, but now move of first player -> so, black is moving
                    blackIsMoving = true
                }

                //if currently black clock is moving - only proceed if it's paused by black AND black side pressed his button again
                if (blackIsMoving) {
                    //player pressed again his button
                    if (it.isPaused) {
                        //only proceed if before pause it was black moving
                        clickOnPause()
                    }
                    return@launch
                } else {
                    //if currently black clock is NOT moving - only proceed if it's NOT paused (by white side player)
                    if (it.isPaused) {
                        //it was paused when white player's clock was going
                        return@launch
                    }
                    handleNewMove(true)
                }
            }
        }
    }


    fun clickOnWhiteButtonView() {
        Timber.d("qwer clickOnWhiteButtonView")
        CoroutineScope(Dispatchers.Main).launch {
            var isTheFirstMove = false
            if (game == null) return@launch
            isTheFirstMove = startFirstMove(true)
            if (isTheFirstMove) return@launch

            game?.let {

                //define which clock is going
                var whiteIsMoving = false
                if (it.isWhiteFirst && it.isFirstPlayerMoving) {//white was first, AND now move of first player -> so, white is moving
                    whiteIsMoving = true
                } else if (!it.isWhiteFirst && !it.isFirstPlayerMoving) {//white was not first, AND now move of not first player -> so, black is moving
                    whiteIsMoving = true
                }

                //if currently white clock is moving - only proceed if it's paused by white AND white side pressed his button again
                if (whiteIsMoving) {
                    //player pressed again his button
                    if (it.isPaused) {
                        //only proceed if before pause it was white moving
                        clickOnPause()
                    }
                    return@launch
                } else {
                    //if currently white clock is NOT moving - only proceed if it's NOT paused (by black side player)
                    if (it.isPaused) {
                        //it was paused when black player's clock was going
                        return@launch
                    }
                    handleNewMove(false)
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
//                val pausedTime = System.currentTimeMillis() - it.pausedStartMillis
//                it.pausedMillis += pausedTime
//                it.pausedStartMillis = 0L

                changedToPauseIconLive.postValue(true)
                if (whiteButtonBGLive.value == 2) {//white is paused
                    whiteButtonBGLive.postValue(1)//white is pressed
                } else if (blackButtonBGLive.value == 2) {//black is paused
                    blackButtonBGLive.postValue(1)//black is pressed
                }
            } else {
                Timber.d("qwer clickOnPause if NOT it.isPaused)")
                //pause time
                timer?.let {
                    it.cancel()
                }
                timer = null
                it.isPaused = true
                //it.pausedStartMillis = System.currentTimeMillis()

                changedToPauseIconLive.postValue(false)
                if (whiteButtonBGLive.value == 1) {//white is pressed
                    whiteButtonBGLive.postValue(2)//white is paused
                } else if (blackButtonBGLive.value == 1) {//black is pressed
                    blackButtonBGLive.postValue(2)//black is paused
                }
            }
        }
    }


    private fun handleNewMove(isWhiteMovingCurrently: Boolean) {
        Timber.d("qwer handleNewMove isWhiteMovingCurrently: %s", isWhiteMovingCurrently)
        game?.let {
            //add increment
            if (it.inc > 0) {
                if (isWhiteMovingCurrently) {
                    Timber.d("qwer handleNewMove whiteRest += it.inc * 1000")
                    it.whiteRest += it.inc * 1000
                } else {
                    Timber.d("qwer handleNewMove blackRest += it.inc * 1000")
                    it.blackRest += it.inc * 1000
                }
            }

            it.systemMillis = System.currentTimeMillis()
            it.isFirstPlayerMoving = !it.isFirstPlayerMoving
            Timber.d("qwer handleNewMove isFirstPlayerMoving: %s", it.isFirstPlayerMoving)
        }

        if (whiteButtonBGLive.value == 1) {//white is pressed
            blackButtonBGLive.postValue(1)//black is pressed
            whiteButtonBGLive.postValue(3)//white is waiting
        } else if (blackButtonBGLive.value == 1) {//black is pressed
            whiteButtonBGLive.postValue(1)//white is pressed
            blackButtonBGLive.postValue(3)//black is waiting
        }

        startTimer()
    }


    private fun startTimer() {
        timer?.let { it.cancel() }
        timer = null

        var isWhiteMovingCurrently = false
        game?.let {

            if (it.isFirstPlayerMoving && it.isWhiteFirst) {
                isWhiteMovingCurrently = true
            } else if (!it.isFirstPlayerMoving && !it.isWhiteFirst) {
                isWhiteMovingCurrently = true
            } else if (!it.isFirstPlayerMoving && it.isWhiteFirst) {
                isWhiteMovingCurrently = false
            } else if (it.isFirstPlayerMoving && !it.isWhiteFirst) {
                isWhiteMovingCurrently = false
            }
        }

        val millFuture = if (isWhiteMovingCurrently) game!!.whiteRest else game!!.blackRest
        Timber.d("qwer startTimer millFuture: %s", millFuture)

        var ticks = 0

        timer = object : CountDownTimer(millFuture, 100) {
            override fun onTick(millisUntilFinished: Long) {

                game?.let {
                    val restTime: Long
                    if (isWhiteMovingCurrently) {
                        it.whiteRest -= 100L
                        if (it.whiteRest < 0L) it.whiteRest = 0L
                        //Timber.d("qwer onTick whiteRest: %s", it.whiteRest)
                        restTime = it.whiteRest
                    } else {
                        it.blackRest -= 100L
                        if (it.blackRest < 0L) it.blackRest = 0L
                        //Timber.d("qwer onTick blackRest: %s", it.blackRest)
                        restTime = it.blackRest
                    }

                    //refresh
                    if (restTime < 20_000) {
                        //refresh every 100ms
                        restTimeWhiteLive.postValue(it.whiteRest)
                        restTimeBlackLive.postValue(it.blackRest)
                    } else {
                        //refresh only every sec
                        if (ticks % 10 == 0) {
                            //Timber.d("qwer startTimer refresh only every sec ticks: %s", ticks)
                            restTimeWhiteLive.postValue(it.whiteRest)
                            restTimeBlackLive.postValue(it.blackRest)
                        }
                    }

                }
                ticks++

            }

            override fun onFinish() {
                Timber.d("qwer startTimer onFinish")
                var sideWhichExpired = "white"

                game?.let {
                    //define which side expired: who started
                    Timber.d(
                        "qwer startTimer onFinish whiteRest: %s, blackRest: %s",
                        it.whiteRest,
                        it.blackRest
                    )
                    if (it.isFirstPlayerMoving) {
                        sideWhichExpired = "white"
                    } else if (!it.isFirstPlayerMoving) {
                        sideWhichExpired = "black"
                    }
//                    //set to 0 rest time as sometimes there is mistake and time is shown 0:00.2 when it is expired
                    if (it.whiteRest < it.blackRest) it.whiteRest = 0L else it.blackRest = 0L
                    //refresh
                    restTimeWhiteLive.postValue(it.whiteRest)
                    restTimeBlackLive.postValue(it.blackRest)
                }

                _timeExpired.postValue(Event(sideWhichExpired))
            }
        }
        timer!!.start()
    }


    private fun startFirstMove(isWhiteFirst: Boolean): Boolean {
        game?.let {
            if (it.systemMillis == 0L) {
                Timber.d("qwer startFirstMove")
                //the game is not started yet
                it.systemMillis = System.currentTimeMillis()
                it.whiteRest = (it.min * 60 + it.sec) * 1000L
                it.blackRest = it.whiteRest
                it.isFirstPlayerMoving = true
                it.isWhiteFirst = isWhiteFirst

                isWhiteFirstLive.postValue(isWhiteFirst)
                if (isWhiteFirst) {
                    whiteButtonBGLive.postValue(1)//white is pressed
                    blackButtonBGLive.postValue(3)//black is waiting
                } else {
                    blackButtonBGLive.postValue(1)//black is pressed
                    whiteButtonBGLive.postValue(3)//white is waiting
                }

                startTimer()

                return true
            }
        }

        return false
    }


    private fun resetGame() {
        game?.let {
            Timber.d("qwer resetGame")
            it.systemMillis = 0L
            //it.pausedMillis = 0L
            //it.pausedStartMillis = 0L
            it.isWhiteFirst = true
            it.isFirstPlayerMoving = true
            it.isPaused = false

            it.whiteRest = (it.min * 60 + it.sec) * 1000L
            it.blackRest = game!!.whiteRest
        }
    }


}