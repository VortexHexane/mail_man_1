package com.vortex.mail_man_1.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import com.vortex.mail_man_1.ui.theme.Raleway

@Composable
fun TimerCircle(
    progress: Float,
    timeText: String,
    progressColor: Color = Color.White,
    modifier: Modifier = Modifier
) {
    // Create animated progress
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "Progress Animation"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(200.dp)
    ) {
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxSize(),
            color = progressColor,
            strokeWidth = 4.dp,
        )
        Text(
            text = timeText,
            style = MaterialTheme.typography.displayLarge.copy(
                fontFamily = Raleway,
                fontWeight = FontWeight.Normal
            ),
            color = progressColor
        )
    }
} 