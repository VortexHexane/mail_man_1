package com.vortex.mail_man_1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vortex.mail_man_1.components.TimerCircle
import com.vortex.mail_man_1.components.TopBar
import com.vortex.mail_man_1.viewmodel.PomodoroState
import com.vortex.mail_man_1.viewmodel.PomodoroViewModel

@Composable
fun PomodoroScreen(viewModel: PomodoroViewModel = viewModel()) {
    val timerState by viewModel.timerState.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(title = "Pomodoro Timer")
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = when (timerState) {
                    is PomodoroState.Initial -> "Ready to Start"
                    is PomodoroState.Work -> "Work Time"
                    is PomodoroState.ShortBreak -> "Short Break"
                    is PomodoroState.LongBreak -> "Long Break"
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            TimerCircle(
                progress = when (timerState) {
                    is PomodoroState.Initial -> 1f
                    else -> timeLeft.toFloat() / viewModel.getTotalTime().toFloat()
                },
                timeText = formatTime(timeLeft),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { 
                        if (timerState is PomodoroState.Initial) {
                            viewModel.startTimer()
                        } else {
                            viewModel.pauseTimer()
                        }
                    }
                ) {
                    Text(if (timerState is PomodoroState.Initial) "Start" else "Pause")
                }

                Button(
                    onClick = { viewModel.resetTimer() }
                ) {
                    Text("Reset")
                }
            }
        }
    }
}

private fun formatTime(timeInMillis: Long): String {
    val minutes = (timeInMillis / 1000) / 60
    val seconds = (timeInMillis / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
} 