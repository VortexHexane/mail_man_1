package com.vortex.mail_man_1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.vortex.mail_man_1.components.TopBar
import com.vortex.mail_man_1.viewmodel.KanbanViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState

@Composable
fun HomeScreen(username: String?, kanbanViewModel: KanbanViewModel) {
    // Refresh counts when the HomeScreen is opened
    LaunchedEffect(Unit) {
        kanbanViewModel.refreshCounts()
    }

    val todoCount by kanbanViewModel.todoCount.collectAsState()
    val doingCount by kanbanViewModel.doingCount.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            // Add TopBar with greeting
            TopBar(title = "Hello ${username ?: "Guest"},")

            // Display counts for each section
            Text("TODO: $todoCount cards")
            Text("DOING: $doingCount cards")
        }
    }
} 