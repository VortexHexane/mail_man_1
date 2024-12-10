package com.vortex.mail_man_1.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    title: String,
    textColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Normal
        ),
        color = textColor,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
    )
} 