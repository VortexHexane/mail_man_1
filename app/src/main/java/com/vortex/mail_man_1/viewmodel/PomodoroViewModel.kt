package com.vortex.mail_man_1.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PomodoroViewModel : ViewModel() {
    companion object {
        const val WORK_TIME = 25 * 60 * 1000L      // 25 minutes
        const val SHORT_BREAK = 5 * 60 * 1000L    // 5 minutes
        const val LONG_BREAK = 20 * 60 * 1000L     // 20 minutes
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

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning = _isTimerRunning.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused = _isPaused.asStateFlow()

    private var pausedTimeLeft: Long = 0  // Store the exact time when paused
    private var lastTimerState: PomodoroState = PomodoroState.Initial  // Store the state when paused

    fun startTimer() {
        if (timer != null) return

        _isTimerRunning.value = true
        
        val duration = if (_isPaused.value && pausedTimeLeft > 0) {
            // Resume from paused time
            _timerState.value = lastTimerState  // Restore the state
            pausedTimeLeft
        } else {
            // Start fresh timer
            _isPaused.value = false
            when (_timerState.value) {
                is PomodoroState.Initial, is PomodoroState.Work -> WORK_TIME
                is PomodoroState.ShortBreak -> SHORT_BREAK
                is PomodoroState.LongBreak -> LONG_BREAK
            }
        }

        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = millisUntilFinished
                pausedTimeLeft = millisUntilFinished  // Update pausedTimeLeft continuously
            }

            override fun onFinish() {
                _isTimerRunning.value = false
                _isPaused.value = false
                pausedTimeLeft = 0  // Reset paused time
                
                when (_timerState.value) {
                    is PomodoroState.Initial, is PomodoroState.Work -> {
                        _completedPomodori.value++
                        currentCycle++
                        if (currentCycle % POMODORO_CYCLES == 0) {
                            _timerState.value = PomodoroState.LongBreak
                            _timeLeft.value = LONG_BREAK
                        } else {
                            _timerState.value = PomodoroState.ShortBreak
                            _timeLeft.value = SHORT_BREAK
                        }
                    }
                    is PomodoroState.ShortBreak, is PomodoroState.LongBreak -> {
                        _timerState.value = PomodoroState.Work
                        _timeLeft.value = WORK_TIME
                    }
                }
                timer = null
            }
        }.start()

        if (!_isPaused.value) {  // Only update state if not resuming from pause
            _timerState.value = when (_timerState.value) {
                is PomodoroState.Initial -> PomodoroState.Work
                else -> _timerState.value
            }
        }
    }

    fun pauseTimer() {
        timer?.cancel()
        timer = null
        _isTimerRunning.value = false
        _isPaused.value = true
        pausedTimeLeft = _timeLeft.value  // Store the exact time left
        lastTimerState = _timerState.value  // Store the current state
    }

    fun resetTimer() {
        timer?.cancel()
        timer = null
        currentCycle = 0
        _completedPomodori.value = 0
        _timerState.value = PomodoroState.Initial
        _timeLeft.value = WORK_TIME
        _isTimerRunning.value = false
        _isPaused.value = false
        pausedTimeLeft = 0  // Reset paused time
        lastTimerState = PomodoroState.Initial  // Reset stored state
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