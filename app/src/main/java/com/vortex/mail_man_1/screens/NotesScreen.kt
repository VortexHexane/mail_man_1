package com.vortex.mail_man_1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vortex.mail_man_1.components.TopBar
import com.vortex.mail_man_1.viewmodel.NotesViewModel

@Composable
fun NotesScreen(
    notesViewModel: NotesViewModel,
    onNavigateToSettings: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(title = "Notes")
        
        // Rest of your notes screen content...
    }
} 