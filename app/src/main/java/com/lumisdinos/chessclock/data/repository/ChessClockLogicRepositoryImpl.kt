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
    override var restTimeBottom = MutableLiveData<Long>()
    override var restTimeTop = MutableLiveData<Long>()
    override var isBottomFirst = MutableLiveData<Boolean>()//pressed first bottomButtonView or topButtonView
    override var topButtonBG = MutableLiveData<Int>()//0 - starting(no pressed); 1 - is not thinking; 2 - is paused; 3 - thinking
    override var bottomButtonBG = MutableLiveData<Int>()//0 - starting(no pressed); 1 - is not thinking; 2 - is paused; 3 - thinking

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
                    restTimeBottom.postValue(it.whiteRest)
                    restTimeTop.postValue(it.blackRest)

                    if (it.systemMillis > 0) {//only consider this if a game is not new
                        if (it.isBottomFirst) {//bottomButton goes first
                            if (it.isFirstPlayerThinking) {//bottomButton first AND white is thinking
                                if (it.isPaused) {
                                    bottomButtonBG.postValue(2)//paused
                                } else {
                                    bottomButtonBG.postValue(3)//thinking
                                }
                                topButtonBG.postValue(1)//not thinking
                            } else {//bottomButton first AND black is moving
                                if (it.isPaused) {
                                    topButtonBG.postValue(2)//paused
                                } else {
                                    topButtonBG.postValue(3)//thinking
                                }
                                bottomButtonBG.postValue(1)//not thinking
                            }
                        } else {//blackButton goes first
                            if (it.isFirstPlayerThinking) {//topButton first AND black is thinking
                                if (it.isPaused) {
                                    topButtonBG.postValue(2)//paused
                                } else {
                                    topButtonBG.postValue(3)//thinking
                                }
                                bottomButtonBG.postValue(3)//not thinking
                            } else {//topButton first AND white is moving
                                if (it.isPaused) {
                                    bottomButtonBG.postValue(2)//paused
                                } else {
                                    bottomButtonBG.postValue(3)//thinking
                                }
                                topButtonBG.postValue(1)//not thinking
                            }
                        }
                    }


                    isBottomFirst.postValue(it.isBottomFirst)

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
                    restTimeBottom.postValue(it.whiteRest)
                    restTimeTop.postValue(it.blackRest)
                    bottomButtonBG.postValue(0)//starting(no pressed)
                    topButtonBG.postValue(0)//starting(no pressed)

                    changedToPauseIcon.postValue(true)
                }

            }
        }
    }


    override fun clickBottomButton(){
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
                if (it.isBottomFirst && !it.isFirstPlayerThinking) {//bottom was first, but now not his move -> so, top is thinking
                    blackIsThinking = true
                } else if (!it.isBottomFirst && it.isFirstPlayerThinking) {//bottom was not first, but now move of first player -> so, top is thinking
                    blackIsThinking = true
                }

                //if currently top clock is moving/thinking - proceed in 2 cases:
                // a) if it's paused by top AND top side pressed his button again
                // b) top pressed his button
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


    override fun clickTopButton(){
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
                if (it.isBottomFirst && it.isFirstPlayerThinking) {//white was first, AND now move of first player -> so, white is whiteIsThinking
                    whiteIsThinking = true
                } else if (!it.isBottomFirst && !it.isFirstPlayerThinking) {//white was not first, AND now move of not first player -> so, white is whiteIsThinking
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
                if (bottomButtonBG.value == 2) {//white is paused
                    bottomButtonBG.postValue(3)//white is thinking
                } else if (topButtonBG.value == 2) {//black is paused
                    topButtonBG.postValue(3)//black is thinking
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
                if (bottomButtonBG.value == 3) {//white is thinking
                    bottomButtonBG.postValue(2)//white is paused
                } else if (topButtonBG.value == 3) {//black is thinking
                    topButtonBG.postValue(2)//black is paused
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

        if (bottomButtonBG.value == 3) {//white WAS thinking
            topButtonBG.postValue(3)//black is thinking
            bottomButtonBG.postValue(1)//white is not thinking
        } else if (topButtonBG.value == 3) {//black WAS thinking
            bottomButtonBG.postValue(3)//white is thinking
            topButtonBG.postValue(1)//black is not thinking
        }

        startTimer()
    }


    private fun startTimer() {
        timer?.let { it.cancel() }
        timer = null

        var isWhiteThinkingCurrently = false
        game?.let {

            if (it.isFirstPlayerThinking && it.isBottomFirst) {
                isWhiteThinkingCurrently = true
            } else if (!it.isFirstPlayerThinking && !it.isBottomFirst) {
                isWhiteThinkingCurrently = true
            } else if (!it.isFirstPlayerThinking && it.isBottomFirst) {
                isWhiteThinkingCurrently = false
            } else if (it.isFirstPlayerThinking && !it.isBottomFirst) {
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
                        restTimeBottom.postValue(it.whiteRest)
                        restTimeTop.postValue(it.blackRest)
                    } else {
                        //refresh only every sec
                        if (ticks % 10 == 0) {
                            restTimeBottom.postValue(it.whiteRest)
                            restTimeTop.postValue(it.blackRest)
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
            restTimeBottom.postValue(it.whiteRest)
            restTimeTop.postValue(it.blackRest)
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
                it.isBottomFirst = isWhiteFirst

                this.isBottomFirst.postValue(isWhiteFirst)
                if (isWhiteFirst) {
                    bottomButtonBG.postValue(1)//white is not thinking
                    topButtonBG.postValue(3)//black is thinking
                } else {
                    topButtonBG.postValue(1)//black is not thinking
                    bottomButtonBG.postValue(3)//white is thinking
                }

                startTimer()

                return true
            }
        }

        return false
    }

}