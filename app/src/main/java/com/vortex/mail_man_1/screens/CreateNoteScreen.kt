package com.vortex.mail_man_1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    Box(modifier = Modifier.fillMaxSize()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Create Note Screen")
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