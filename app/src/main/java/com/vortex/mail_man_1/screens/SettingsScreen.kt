package com.vortex.mail_man_1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vortex.mail_man_1.components.TopBar

/**
 * Settings screen that displays user preferences and logout option
 * @param onNavigateBack Callback to handle navigation back
 * @param onSignOut Callback to handle sign out action
 */
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onSignOut: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(title = "Settings")
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(height = 16.dp))
            Button(onClick = onSignOut) {
                Text("Sign Out")
            }
        }
    }
} 