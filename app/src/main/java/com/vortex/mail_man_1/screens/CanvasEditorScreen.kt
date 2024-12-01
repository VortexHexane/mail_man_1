package com.vortex.mail_man_1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vortex.mail_man_1.viewmodel.NotesViewModel

@Composable
fun CanvasEditorScreen(
    viewModel: NotesViewModel,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    // TODO: Implement actual canvas functionality
    val canvasUri = "placeholder_uri"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Canvas Editor - Coming Soon")
        // TODO: Implement drawing canvas functionality
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { 
                if (title.isNotBlank()) {
                    viewModel.addCanvasNote(title, canvasUri)
                    onNavigateBack()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotBlank()
        ) {
            Text("Save Canvas")
        }
    }
} 