package com.lumisdinos.chessclock.data

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.lumisdinos.chessclock.common.Event
import com.lumisdinos.chessclock.common.utils.strToInt
import com.lumisdinos.chessclock.data.Constants.BLACK_PAUSING_BG
import com.lumisdinos.chessclock.data.Constants.BLACK_THINKING_BG
import com.lumisdinos.chessclock.data.Constants.BLACK_WAITING_BG
import com.lumisdinos.chessclock.data.Constants.STARTING_BG
import com.lumisdinos.chessclock.data.Constants.WHITE_PAUSING_BG
import com.lumisdinos.chessclock.data.Constants.WHITE_THINKING_BG
import com.lumisdinos.chessclock.data.Constants.WHITE_WAITING_BG
import com.lumisdinos.chessclock.data.model.Game
import com.lumisdinos.chessclock.data.repository.ChessClockLogicRepository
import com.lumisdinos.chessclock.data.repository.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChessClockLogicRepositoryImpl @Inject constructor(
    private val gameRepository: GameRepository
) : ChessClockLogicRepository {

    override var timeControl = MutableLiveData<String>()
    override var changedToPauseIcon = MutableLiveData<Boolean>()
    override var restTimeBottom = MutableLiveData<Long>()
    override var restTimeTop = MutableLiveData<Long>()
    override var isBottomPressedFirst = MutableLiveData<Boolean>()
    override var topButtonBG = MutableLiveData<String>()
    override var bottomButtonBG = MutableLiveData<String>()

    override var timeExpired = MutableLiveData<Event<String>>()
    override var moveSound = MutableLiveData<Event<Boolean>>()

    var game: Game? = null
    var timer: CountDownTimer? = null


    override fun createGame() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                game = gameRepository.getFirstGame()
                game?.let {
                    setButtonBg(it.systemMillis == 0L)

                    val bottomTime = if (it.isBottomFirst) it.whiteRest else it.blackRest
                    val topTime = if (it.isBottomFirst) it.blackRest else it.whiteRest

                    restTimeBottom.postValue(bottomTime)
                    restTimeTop.postValue(topTime)
                    isBottomPressedFirst.postValue(it.isBottomFirst)

                    if (it.isPaused) {
                        changedToPauseIcon.postValue(false)
                    } else {
                        changedToPauseIcon.postValue(true)
                    }

                    timeControl.postValue("${it.min}, ${it.sec}, ${it.inc}")
                }

            }

            game?.let {
                if (!it.isPaused && it.systemMillis > 0) {
                    startTimer()
                }
            }

        }
    }


    override fun saveGame() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertIfNotEmptyTime(game)
            }
        }
    }


    override fun setChosenTimeControl(timeCtrl: String) {
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

                    val bottomTime = if (it.isBottomFirst) it.whiteRest else it.blackRest
                    val topTime = if (it.isBottomFirst) it.blackRest else it.whiteRest

                    timeControl.postValue("${it.min}, ${it.sec}, ${it.inc}")
                    restTimeBottom.postValue(bottomTime)
                    restTimeTop.postValue(topTime)
                    setButtonBg(true)

                    changedToPauseIcon.postValue(true)
                }

            }
        }
    }


    override fun clickMoveButton(isBottomPressed: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            game?.let {
                if (it.isGameFinished) {
                    return@launch
                }
            }

            if (isStartingFirstMove(isBottomPressed)) {
                moveSound.postValue(Event(true))
                return@launch
            }

            game?.let {

                var bottomIsThinking = false

                if (it.isBottomFirst && it.isWhitePlayerThinking) {
                    bottomIsThinking = true
                } else if (!it.isBottomFirst && !it.isWhitePlayerThinking) {
                    bottomIsThinking = true
                }

                if (bottomIsThinking && isBottomPressed ||
                    !bottomIsThinking && !isBottomPressed
                ) {
                    if (it.isPaused) {
                        clickPause()
                    } else {
                        moveSound.postValue(Event(true))
                        handleNewMove()
                    }
                    return@launch
                }
            }
        }
    }


    override fun clickPause() {
        game?.let {
            if (it.isGameFinished) {
                return
            }

            if (it.isPaused) {
                //start again
                it.isPaused = false
                startTimer()
                changedToPauseIcon.postValue(true)
            } else {
                //pause time
                timer?.let {
                    it.cancel()
                }
                timer = null
                it.isPaused = true
                //it.pausedStartMillis = System.currentTimeMillis()

                changedToPauseIcon.postValue(false)
            }

            setButtonBg()
        }
    }


    private fun handleNewMove() {
        game?.let {
            //add increment
            if (it.inc > 0) {
                if (it.isWhitePlayerThinking) {
                    it.whiteRest += it.inc * 1000
                } else {
                    it.blackRest += it.inc * 1000
                }
            }

            it.systemMillis = System.currentTimeMillis()
            it.isWhitePlayerThinking = !it.isWhitePlayerThinking
        }

        setButtonBg()
        startTimer()
    }


    private fun startTimer() {
        timer?.let { it.cancel() }
        timer = null

        var millFuture = 0L
        game?.let {
            millFuture = if (it.isWhitePlayerThinking) it.whiteRest else it.blackRest
            it.tickSystemMillis = 0
        }


        var ticks = 0

        timer = object : CountDownTimer(millFuture, 100) {
            override fun onTick(millisUntilFinished: Long) {
                game?.let {
                    val restTime: Long

                    if (it.tickSystemMillis == 0L) {
                        it.tickSystemMillis = System.currentTimeMillis() - 4
                    }

                    if (it.isWhitePlayerThinking) {
                        it.whiteRest =
                            millFuture - (System.currentTimeMillis() - it.tickSystemMillis)
                        if (it.whiteRest < 0L) it.whiteRest = 0L
                        restTime = it.whiteRest
                    } else {
                        it.blackRest =
                            millFuture - (System.currentTimeMillis() - it.tickSystemMillis)
                        if (it.blackRest < 0L) it.blackRest = 0L
                        restTime = it.blackRest
                    }

                    val bottomTime = if (it.isBottomFirst) it.whiteRest else it.blackRest
                    val topTime = if (it.isBottomFirst) it.blackRest else it.whiteRest

                    if (restTime < 20_000) {
                        //refresh every 100ms
                        restTimeBottom.postValue(bottomTime)
                        restTimeTop.postValue(topTime)
                    } else {
                        //refresh only every sec
                        if (ticks % 10 == 0) {
                            restTimeBottom.postValue(bottomTime)
                            restTimeTop.postValue(topTime)
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
            it.isGameFinished = true
            if (it.isWhitePlayerThinking) {
                sideWhichExpired = "white"
            } else if (!it.isWhitePlayerThinking) {
                sideWhichExpired = "black"
            }
            //set to 0 rest time as sometimes there is mistake and time is shown 0:00.2 when it is expired
            if (it.whiteRest < it.blackRest) it.whiteRest = 0L else it.blackRest = 0L

            val bottomTime = if (it.isBottomFirst) it.whiteRest else it.blackRest
            val topTime = if (it.isBottomFirst) it.blackRest else it.whiteRest
            restTimeBottom.postValue(bottomTime)
            restTimeTop.postValue(topTime)
        }

        timeExpired.postValue(Event(sideWhichExpired))
    }


    private fun isStartingFirstMove(isBottomFirst: Boolean): Boolean {
        game?.let {
            if (it.systemMillis == 0L) {
                it.systemMillis = System.currentTimeMillis()
                it.whiteRest = (it.min * 60 + it.sec) * 1000L
                it.blackRest = it.whiteRest
                it.isWhitePlayerThinking = false
                it.isGameFinished = false
                it.isBottomFirst = isBottomFirst
                it.isPaused = false

                this.isBottomPressedFirst.postValue(isBottomFirst)
                setButtonBg()
                startTimer()

                return true
            }
        }

        return false
    }


    private fun setButtonBg(isStarting: Boolean = false) {
        game?.let {

            if (isStarting) {
                bottomButtonBG.postValue(STARTING_BG)
                topButtonBG.postValue(STARTING_BG)
                return
            }

            //bottom button is white
            if (it.isBottomFirst && it.isWhitePlayerThinking && it.isPaused) {
                bottomButtonBG.postValue(WHITE_PAUSING_BG)
                topButtonBG.postValue(BLACK_WAITING_BG)
                return
            }

            if (it.isBottomFirst && it.isWhitePlayerThinking && !it.isPaused) {
                bottomButtonBG.postValue(WHITE_THINKING_BG)
                topButtonBG.postValue(BLACK_WAITING_BG)
                return
            }

            if (it.isBottomFirst && !it.isWhitePlayerThinking && it.isPaused) {
                bottomButtonBG.postValue(WHITE_WAITING_BG)
                topButtonBG.postValue(BLACK_PAUSING_BG)
                return
            }

            if (it.isBottomFirst && !it.isWhitePlayerThinking && !it.isPaused) {
                bottomButtonBG.postValue(WHITE_WAITING_BG)
                topButtonBG.postValue(BLACK_THINKING_BG)
                return
            }

            //top button is white
            if (!it.isBottomFirst && it.isWhitePlayerThinking && it.isPaused) {
                bottomButtonBG.postValue(BLACK_WAITING_BG)
                topButtonBG.postValue(WHITE_PAUSING_BG)
                return
            }

            if (!it.isBottomFirst && it.isWhitePlayerThinking && !it.isPaused) {
                bottomButtonBG.postValue(BLACK_WAITING_BG)
                topButtonBG.postValue(WHITE_THINKING_BG)
                return
            }

            if (!it.isBottomFirst && !it.isWhitePlayerThinking && it.isPaused) {
                bottomButtonBG.postValue(BLACK_PAUSING_BG)
                topButtonBG.postValue(WHITE_WAITING_BG)
                return
            }

            if (!it.isBottomFirst && !it.isWhitePlayerThinking && !it.isPaused) {
                bottomButtonBG.postValue(BLACK_THINKING_BG)
                topButtonBG.postValue(WHITE_WAITING_BG)
                return
            }
        }
    }

}