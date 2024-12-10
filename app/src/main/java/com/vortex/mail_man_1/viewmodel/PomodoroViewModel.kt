package com.vortex.mail_man_1.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PomodoroViewModel : ViewModel() {
    companion object {
        const val WORK_TIME = 25 * 60 * 1000L      // 25 minutes
        const val SHORT_BREAK = 10 * 60 * 1000L    // 10 minutes
        const val LONG_BREAK = 30 * 60 * 1000L     // 30 minutes
        const val POMODORO_CYCLES = 4              // Number of work sessions before long break
    }

    private var timer: CountDownTimer? = null
    private var currentCycle = 0
    
    private val _timerState = MutableStateFlow<PomodoroState>(PomodoroState.Initial)
    val timerState = _timerState.asStateFlow()

    private val _timeLeft = MutableStateFlow(WORK_TIME)
    val timeLeft = _timeLeft.asStateFlow()

    private val _completedPomodori = MutableStateFlow(0)
    val completedPomodori = _completedPomodori.asStateFlow()

    fun startTimer() {
        if (timer != null) return

        val duration = when (_timerState.value) {
            is PomodoroState.Initial, is PomodoroState.Work -> WORK_TIME
            is PomodoroState.ShortBreak -> SHORT_BREAK
            is PomodoroState.LongBreak -> LONG_BREAK
        }

        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = millisUntilFinished
            }

            override fun onFinish() {
                when (_timerState.value) {
                    is PomodoroState.Initial, is PomodoroState.Work -> {
                        _completedPomodori.value++
                        currentCycle++
                        if (currentCycle % POMODORO_CYCLES == 0) {
                            // After 4 pomodori, take a long break
                            _timerState.value = PomodoroState.LongBreak
                            _timeLeft.value = LONG_BREAK
                        } else {
                            // After each pomodoro, take a short break
                            _timerState.value = PomodoroState.ShortBreak
                            _timeLeft.value = SHORT_BREAK
                        }
                    }
                    is PomodoroState.ShortBreak, is PomodoroState.LongBreak -> {
                        // After any break, start a new work session
                        _timerState.value = PomodoroState.Work
                        _timeLeft.value = WORK_TIME
                    }
                }
                timer = null
            }
        }.start()

        // Update state when starting timer
        _timerState.value = when (_timerState.value) {
            is PomodoroState.Initial -> PomodoroState.Work
            else -> _timerState.value
        }
    }

    fun pauseTimer() {
        timer?.cancel()
        timer = null
    }

    fun resetTimer() {
        timer?.cancel()
        timer = null
        currentCycle = 0
        _completedPomodori.value = 0
        _timerState.value = PomodoroState.Initial
        _timeLeft.value = WORK_TIME
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    fun getTotalTime(): Long {
        return when (timerState.value) {
            is PomodoroState.Work -> WORK_TIME
            is PomodoroState.ShortBreak -> SHORT_BREAK
            is PomodoroState.LongBreak -> LONG_BREAK
            is PomodoroState.Initial -> WORK_TIME
        }
    }

    fun getCurrentCycle(): Int = currentCycle
}

sealed class PomodoroState {
    object Initial : PomodoroState()
    object Work : PomodoroState()
    object ShortBreak : PomodoroState()
    object LongBreak : PomodoroState()
} 