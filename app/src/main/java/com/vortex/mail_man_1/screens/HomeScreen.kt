package com.vortex.mail_man_1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vortex.mail_man_1.components.TopBar

@Composable
fun HomeScreen(username: String?) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            // Add TopBar with greeting
            TopBar(title = "Hello ${username ?: "Guest"},")
            

        }
    }
} 