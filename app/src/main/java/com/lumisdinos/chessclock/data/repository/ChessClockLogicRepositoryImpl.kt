package com.lumisdinos.chessclock.data.repository

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.lumisdinos.chessclock.common.Event
import com.lumisdinos.chessclock.common.utils.strToInt
import com.lumisdinos.chessclock.data.model.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ChessClockLogicRepositoryImpl @Inject constructor(
    private val gameRepository: GameRepository
) : ChessClockLogicRepository {

    override var timeControl = MutableLiveData<String>()
    override var changedToPauseIcon = MutableLiveData<Boolean>()
    override var restTimeWhite = MutableLiveData<Long>()
    override var restTimeBlack = MutableLiveData<Long>()
    override var isWhiteFirst = MutableLiveData<Boolean>()//pressed first blackButtonView or blackButtonView
    override var blackButtonBG = MutableLiveData<Int>()//0 - starting(no pressed); 1 - is not thinking; 2 - is paused; 3 - thinking
    override var whiteButtonBG = MutableLiveData<Int>()//0 - starting(no pressed); 1 - is not thinking; 2 - is paused; 3 - thinking

    override var timeExpired = MutableLiveData<Event<String>>()
    override var moveSound = MutableLiveData<Event<Boolean>>()

    var game: Game? = null
    var timer: CountDownTimer? = null


    override fun createGame() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                game = gameRepository.getFirstGame()
                game?.let {
                    Timber.d("qwer getGame: %s", game)

                    //set live vars
                    restTimeWhite.postValue(it.whiteRest)
                    restTimeBlack.postValue(it.blackRest)

                    if (it.systemMillis > 0) {//only consider this if a game is not new
                        if (it.isWhiteFirst) {//whiteButton goes first
                            if (it.isFirstPlayerThinking) {//whiteButton first AND white is thinking
                                if (it.isPaused) {
                                    whiteButtonBG.postValue(2)//paused
                                } else {
                                    whiteButtonBG.postValue(3)//thinking
                                }
                                blackButtonBG.postValue(1)//not thinking
                            } else {//whiteButton first AND black is moving
                                if (it.isPaused) {
                                    blackButtonBG.postValue(2)//paused
                                } else {
                                    blackButtonBG.postValue(3)//thinking
                                }
                                whiteButtonBG.postValue(1)//not thinking
                            }
                        } else {//blackButton goes first
                            if (it.isFirstPlayerThinking) {//blackButton first AND black is thinking
                                if (it.isPaused) {
                                    blackButtonBG.postValue(2)//paused
                                } else {
                                    blackButtonBG.postValue(3)//thinking
                                }
                                whiteButtonBG.postValue(3)//not thinking
                            } else {//blackButton first AND white is moving
                                if (it.isPaused) {
                                    whiteButtonBG.postValue(2)//paused
                                } else {
                                    whiteButtonBG.postValue(3)//thinking
                                }
                                blackButtonBG.postValue(1)//not thinking
                            }
                        }
                    }


                    isWhiteFirst.postValue(it.isWhiteFirst)

                    if (it.isPaused) {
                        changedToPauseIcon.postValue(false)
                    } else {
                        changedToPauseIcon.postValue(true)
                    }

                    timeControl.postValue("${it.min}, ${it.sec}, ${it.inc}")
                }//game?.let

            }

            game?.let {
                if (!it.isPaused && it.systemMillis > 0) {
                    startTimer()
                }
            }

        }
    }


    override fun saveGame(){
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertIfNotEmptyTime(game)
            }
        }
    }


    override fun setChosenTimeControl(timeCtrl: String){
        //"15, 0, 10"
        CoroutineScope(Dispatchers.Main).launch {
            timer?.let { it.cancel() }
            timer = null
            withContext(Dispatchers.IO) {
                val times = timeCtrl.split(",").map { it.trim() }

                game?.let {
                    //set new time control
                    it.min = strToInt(times[0])
                    it.sec = strToInt(times[1])
                    it.inc = strToInt(times[2])

                    gameRepository.resetGame(game)

                    timeControl.postValue("${it.min}, ${it.sec}, ${it.inc}")
                    restTimeWhite.postValue(it.whiteRest)
                    restTimeBlack.postValue(it.blackRest)
                    whiteButtonBG.postValue(0)//starting(no pressed)
                    blackButtonBG.postValue(0)//starting(no pressed)

                    changedToPauseIcon.postValue(true)
                }

            }
        }
    }


    override fun clickBlackButton(){
        Timber.d("qwer clickOnBlackButtonView")
        CoroutineScope(Dispatchers.Main).launch {
            game?.let {
                if (it.isGameFinished) {
                    return@launch
                }
            }

            var isTheFirstMove = false
            if (game == null) return@launch
            isTheFirstMove = startFirstMove(false)
            if (isTheFirstMove) {
                moveSound.postValue(Event(true))
                return@launch
            }

            game?.let {

                //define which clock is going
                var blackIsThinking = false
                if (it.isWhiteFirst && !it.isFirstPlayerThinking) {//white was first, but now not his move -> so, black is thinking
                    blackIsThinking = true
                } else if (!it.isWhiteFirst && it.isFirstPlayerThinking) {//white was not first, but now move of first player -> so, black is thinking
                    blackIsThinking = true
                }

                //if currently black clock is moving/thinking - proceed in 2 cases:
                // a) if it's paused by black AND black side pressed his button again
                // b) black pressed his button
                if (blackIsThinking) {
                    if (it.isPaused) {
                        //only proceed if before pause it was black thinking
                        clickPause()
                    } else {
                        moveSound.postValue(Event(true))
                        handleNewMove(false)
                    }
                    return@launch
                }
            }
        }
    }


    override fun clickWhiteButton(){
        Timber.d("qwer clickOnWhiteButtonView")
        CoroutineScope(Dispatchers.Main).launch {
            game?.let {
                if (it.isGameFinished) {
                    return@launch
                }
            }

            var isTheFirstMove = false
            if (game == null) return@launch
            isTheFirstMove = startFirstMove(true)
            if (isTheFirstMove) {
                moveSound.postValue(Event(true))
                return@launch
            }

            game?.let {

                //define which clock is going
                var whiteIsThinking = false
                if (it.isWhiteFirst && it.isFirstPlayerThinking) {//white was first, AND now move of first player -> so, white is whiteIsThinking
                    whiteIsThinking = true
                } else if (!it.isWhiteFirst && !it.isFirstPlayerThinking) {//white was not first, AND now move of not first player -> so, white is whiteIsThinking
                    whiteIsThinking = true
                }

                //if currently white clock is moving/thinking - proceed in 2 cases:
                // a) if it's paused by white AND white side pressed his button again
                // b) white pressed his button
                if (whiteIsThinking) {
                    if (it.isPaused) {
                        //only proceed if before pause it was white moving
                        clickPause()
                    } else {
                        moveSound.postValue(Event(true))
                        handleNewMove(true)
                    }
                    return@launch
                }
            }
        }
    }


    override fun clickPause(){
        game?.let {
            if (it.isGameFinished) {
                return
            }

            if (it.isPaused) {
                Timber.d("qwer clickOnPause if (it.isPaused)")
                //start again
                it.isPaused = false
                startTimer()
                //add paused time
//                val pausedTime = System.currentTimeMillis() - it.pausedStartMillis
//                it.pausedMillis += pausedTime
//                it.pausedStartMillis = 0L

                changedToPauseIcon.postValue(true)
                if (whiteButtonBG.value == 2) {//white is paused
                    whiteButtonBG.postValue(3)//white is thinking
                } else if (blackButtonBG.value == 2) {//black is paused
                    blackButtonBG.postValue(3)//black is thinking
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

                changedToPauseIcon.postValue(false)
                if (whiteButtonBG.value == 3) {//white is thinking
                    whiteButtonBG.postValue(2)//white is paused
                } else if (blackButtonBG.value == 3) {//black is thinking
                    blackButtonBG.postValue(2)//black is paused
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
                    it.whiteRest += it.inc * 1000
                } else {
                    it.blackRest += it.inc * 1000
                }
            }

            it.systemMillis = System.currentTimeMillis()
            it.isFirstPlayerThinking = !it.isFirstPlayerThinking
        }

        if (whiteButtonBG.value == 3) {//white WAS thinking
            blackButtonBG.postValue(3)//black is thinking
            whiteButtonBG.postValue(1)//white is not thinking
        } else if (blackButtonBG.value == 3) {//black WAS thinking
            whiteButtonBG.postValue(3)//white is thinking
            blackButtonBG.postValue(1)//black is not thinking
        }

        startTimer()
    }


    private fun startTimer() {
        timer?.let { it.cancel() }
        timer = null

        var isWhiteThinkingCurrently = false
        game?.let {

            if (it.isFirstPlayerThinking && it.isWhiteFirst) {
                isWhiteThinkingCurrently = true
            } else if (!it.isFirstPlayerThinking && !it.isWhiteFirst) {
                isWhiteThinkingCurrently = true
            } else if (!it.isFirstPlayerThinking && it.isWhiteFirst) {
                isWhiteThinkingCurrently = false
            } else if (it.isFirstPlayerThinking && !it.isWhiteFirst) {
                isWhiteThinkingCurrently = false
            }
        }

        val millFuture = if (isWhiteThinkingCurrently) game!!.whiteRest else game!!.blackRest

        var ticks = 0
        game?.let {
            it.tickSystemMillis = 0
        }

        timer = object : CountDownTimer(millFuture, 100) {
            override fun onTick(millisUntilFinished: Long) {
                game?.let {
                    val restTime: Long

                    if (it.tickSystemMillis == 0L) {
                        it.tickSystemMillis = System.currentTimeMillis() - 4
                    }


                    if (isWhiteThinkingCurrently) {
                        it.whiteRest = millFuture - (System.currentTimeMillis() - it.tickSystemMillis)
                        if (it.whiteRest < 0L) it.whiteRest = 0L
                        restTime = it.whiteRest
                    } else {
                        it.blackRest = millFuture - (System.currentTimeMillis() - it.tickSystemMillis)
                        if (it.blackRest < 0L) it.blackRest = 0L
                        restTime = it.blackRest
                    }

                    //refresh
                    if (restTime < 20_000) {
                        //refresh every 100ms
                        restTimeWhite.postValue(it.whiteRest)
                        restTimeBlack.postValue(it.blackRest)
                    } else {
                        //refresh only every sec
                        if (ticks % 10 == 0) {
                            restTimeWhite.postValue(it.whiteRest)
                            restTimeBlack.postValue(it.blackRest)
                        }
                    }

                }
                ticks++

            }

            override fun onFinish() {
                onFinishTime()
            }
        }
        timer!!.start()
    }


    private fun onFinishTime() {
        var sideWhichExpired = "white"

        game?.let {
            //define which side expired: who started
            it.isGameFinished = true
            if (it.isFirstPlayerThinking) {
                sideWhichExpired = "white"
            } else if (!it.isFirstPlayerThinking) {
                sideWhichExpired = "black"
            }
//                    //set to 0 rest time as sometimes there is mistake and time is shown 0:00.2 when it is expired
            if (it.whiteRest < it.blackRest) it.whiteRest = 0L else it.blackRest = 0L
            //refresh
            restTimeWhite.postValue(it.whiteRest)
            restTimeBlack.postValue(it.blackRest)
        }

        timeExpired.postValue(Event(sideWhichExpired))
    }


    private fun startFirstMove(isWhiteFirst: Boolean): Boolean {
        game?.let {
            if (it.systemMillis == 0L) {
                //the game is not started yet
                it.systemMillis = System.currentTimeMillis()
                it.whiteRest = (it.min * 60 + it.sec) * 1000L
                it.blackRest = it.whiteRest
                it.isFirstPlayerThinking = false
                it.isGameFinished = false
                it.isWhiteFirst = isWhiteFirst

                this.isWhiteFirst.postValue(isWhiteFirst)
                if (isWhiteFirst) {
                    whiteButtonBG.postValue(1)//white is not thinking
                    blackButtonBG.postValue(3)//black is thinking
                } else {
                    blackButtonBG.postValue(1)//black is not thinking
                    whiteButtonBG.postValue(3)//white is thinking
                }

                startTimer()

                return true
            }
        }

        return false
    }

}