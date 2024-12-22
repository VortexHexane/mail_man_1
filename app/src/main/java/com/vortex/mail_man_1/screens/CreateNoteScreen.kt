package com.vortex.mail_man_1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vortex.mail_man_1.components.TopBar
import com.vortex.mail_man_1.viewmodel.NotesViewModel

/**
 * Screen for creating new notes
 * @param onNavigateBack Callback to handle navigation back
 * @param viewModel ViewModel instance for handling note operations
 */
@Composable
fun CreateNoteScreen(
    onNavigateBack: () -> Unit,
    viewModel: NotesViewModel
) {
    val notes = viewModel.notes.collectAsState(initial = emptyList()).value

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TopBar(title = "Notes")
                
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (notes.isEmpty()) {
                        Text(
                            text = "Press \"+\" button to add a note",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { /* TODO: Add action */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(all = 10.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add"
            )
        }
    }
} 