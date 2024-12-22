package com.vortex.mail_man_1.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vortex.mail_man_1.components.TopBar
import com.vortex.mail_man_1.model.Note
import com.vortex.mail_man_1.model.TextNote
import com.vortex.mail_man_1.model.ChecklistNote
import com.vortex.mail_man_1.model.CanvasNote
import com.vortex.mail_man_1.viewmodel.NotesViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.graphics.Color

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
    var showInputCard by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var noteType by remember { mutableStateOf("") }
    var currentNoteId by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TopBar(title = "Notes")
                
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    if (notes.isEmpty() && !showInputCard) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Press \"+\" button to add a note",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(notes) { note ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            width = 1.dp,
                                            color = Color(0xFF64B5F6),
                                            shape = MaterialTheme.shapes.medium
                                        )
                                        .clickable { 
                                            noteTitle = note.title
                                            when (note) {
                                                is TextNote -> {
                                                    noteType = "note"
                                                    noteContent = note.content
                                                }
                                                is ChecklistNote -> {
                                                    noteType = "checklist"
                                                    // Handle checklist content if needed
                                                }
                                                else -> {
                                                    noteType = "note"
                                                    noteContent = ""
                                                }
                                            }
                                            currentNoteId = note.id
                                            showInputCard = true
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.Black
                                    )
                                ) {
                                    NoteCard(
                                        note = note,
                                        onDelete = { viewModel.deleteNote(note.id) }
                                    )
                                }
                            }
                        }
                    }

                    if (showInputCard) {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                OutlinedTextField(
                                    value = noteTitle,
                                    onValueChange = { noteTitle = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text("Title") }
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = noteContent,
                                    onValueChange = { noteContent = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text("Content") },
                                    minLines = 3
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    TextButton(
                                        onClick = { showDeleteDialog = true },
                                        colors = ButtonDefaults.textButtonColors(
                                            contentColor = MaterialTheme.colorScheme.error
                                        )
                                    ) {
                                        Text("Delete")
                                    }

                                    Button(
                                        onClick = {
                                            if (noteTitle.isNotBlank()) {
                                                if (currentNoteId != null) {
                                                    viewModel.updateNote(currentNoteId!!, noteTitle, noteContent)
                                                } else {
                                                    when (noteType) {
                                                        "note" -> viewModel.addTextNote(noteTitle, noteContent)
                                                        "checklist" -> {
                                                            val items = noteContent.split("\n").filter { it.isNotBlank() }
                                                            viewModel.addChecklistNote(noteTitle, items)
                                                        }
                                                    }
                                                }
                                                noteTitle = ""
                                                noteContent = ""
                                                showInputCard = false
                                                currentNoteId = null
                                            }
                                        },
                                        enabled = noteTitle.isNotBlank()
                                    ) {
                                        Text("Save")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Delete confirmation dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Confirm Delete") },
                text = { Text("Are you sure you want to delete this note?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (currentNoteId != null) {
                                viewModel.deleteNote(currentNoteId!!)
                                currentNoteId = null
                            }
                            showDeleteDialog = false
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Floating Action Button with Dropdown Menu
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = { showMenu = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note"
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Note") },
                    onClick = {
                        noteType = "note"
                        showInputCard = true
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Checklist") },
                    onClick = {
                        noteType = "checklist"
                        showInputCard = true
                        showMenu = false
                    }
                )
            }
        }
    }
}

@Composable
private fun NoteCard(
    note: Note,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showViewCard by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = note.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
            color = Color.White
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = dateFormat.format(note.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f)
            )

            TextButton(
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete")
            }
        }
    }

    // View Note Dialog
    if (showViewCard) {
        AlertDialog(
            onDismissRequest = { showViewCard = false },
            title = { Text(note.title) },
            text = {
                Column {
                    when (note) {
                        is TextNote -> Text(note.content)
                        is ChecklistNote -> {
                            note.items.forEach { item ->
                                Row(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(item.text)
                                }
                            }
                        }
                        is CanvasNote -> {
                            Text("Canvas note preview not available")
                        }
                        else -> {
                            Text("Unsupported note type")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Created: ${dateFormat.format(note.timestamp)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showViewCard = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this note?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
} 