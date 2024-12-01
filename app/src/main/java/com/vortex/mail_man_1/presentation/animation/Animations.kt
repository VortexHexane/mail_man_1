package com.vortex.mail_man_1.presentation.animation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition

object Animations {
    fun slideIn(
        initialOffsetX: Int = 300,
        durationMillis: Int = 300
    ): EnterTransition = slideInHorizontally(
        animationSpec = tween(durationMillis)
    ) { initialOffsetX }

    fun slideOut(
        targetOffsetX: Int = -300,
        durationMillis: Int = 300
    ): ExitTransition = slideOutHorizontally(
        animationSpec = tween(durationMillis)
    ) { targetOffsetX }

    // Add more animations as needed
} 