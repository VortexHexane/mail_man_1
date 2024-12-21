package com.vortex.mail_man_1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vortex.mail_man_1.components.TimerCircle
import com.vortex.mail_man_1.components.TopBar
import com.vortex.mail_man_1.ui.theme.PomodoroWorkGreen
import com.vortex.mail_man_1.ui.theme.PomodoroBreakRed
import com.vortex.mail_man_1.ui.theme.PomodoroLongBreakBlue
import com.vortex.mail_man_1.ui.theme.PomodoroBackgroundBlack
import com.vortex.mail_man_1.viewmodel.PomodoroState
import com.vortex.mail_man_1.viewmodel.PomodoroViewModel
import com.vortex.mail_man_1.ui.theme.Raleway

@Composable
fun PomodoroScreen(viewModel: PomodoroViewModel = viewModel()) {
    val timerState by viewModel.timerState.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()
    val isPaused by viewModel.isPaused.collectAsState()

    val backgroundColor = when (timerState) {
        is PomodoroState.Work -> PomodoroWorkGreen
        is PomodoroState.ShortBreak -> PomodoroBreakRed
        is PomodoroState.LongBreak -> PomodoroLongBreakBlue
        is PomodoroState.Initial -> PomodoroBackgroundBlack
    }

    val contentColor = Color.White

    val buttonTextColor = Color.Black

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBar(
                title = "Pomodoro Timer",
                textColor = contentColor
            )
            
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
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = Raleway,
                        fontWeight = FontWeight.Normal
                    ),
                    color = contentColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                TimerCircle(
                    progress = when (timerState) {
                        is PomodoroState.Initial -> 1f
                        else -> timeLeft.toFloat() / viewModel.getTotalTime().toFloat()
                    },
                    timeText = formatTime(timeLeft),
                    progressColor = contentColor,
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
                            if (!isTimerRunning) {
                                viewModel.startTimer()
                            } else {
                                viewModel.pauseTimer()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = contentColor,
                            contentColor = buttonTextColor
                        )
                    ) {
                        Text(
                            text = when {
                                isTimerRunning -> "Pause"
                                isPaused -> "Continue"
                                else -> "Start"
                            },
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }

                    Button(
                        onClick = { viewModel.resetTimer() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = contentColor,
                            contentColor = buttonTextColor
                        )
                    ) {
                        Text(
                            text = "Reset",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Cycle: ${viewModel.getCurrentCycle() + 1}/${PomodoroViewModel.POMODORO_CYCLES}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = Raleway,
                        fontWeight = FontWeight.Normal
                    ),
                    color = contentColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

private fun formatTime(timeInMillis: Long): String {
    val minutes = (timeInMillis / 1000) / 60
    val seconds = (timeInMillis / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
} 