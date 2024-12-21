package com.vortex.mail_man_1.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vortex.mail_man_1.model.KanbanCard
import com.vortex.mail_man_1.model.KanbanSection
import com.vortex.mail_man_1.viewmodel.KanbanViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.vortex.mail_man_1.components.TopBar
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun KanbanBoardScreen(viewModel: KanbanViewModel = viewModel()) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(title = "Kanban Board")
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
        ) {
            val cards by viewModel.cards.collectAsState()
            val selectedSection by viewModel.selectedSection.collectAsState()
            val errorMessage by viewModel.errorMessage.collectAsState()
            var newCardText by remember { mutableStateOf("") }

            // Section Switch
            KanbanSectionSwitch(
                selectedSection = selectedSection,
                onSectionSelected = { viewModel.selectSection(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add new card section
            OutlinedTextField(
                value = newCardText,
                onValueChange = { newCardText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("New Task") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (newCardText.isNotBlank()) {
                                viewModel.addCard(newCardText)
                                newCardText = ""
                            }
                        }
                    ) {
                        Icon(Icons.Default.Add, "Add Task")
                    }
                }
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cards list with animation
            AnimatedContent(
                targetState = selectedSection,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { if (targetState.ordinal > initialState.ordinal) it else -it }
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { if (targetState.ordinal > initialState.ordinal) -it else it }
                    )
                }
            ) { section ->
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cards.filter { it.section == section }) { card ->
                        KanbanCard(
                            card = card,
                            onMove = { newSection -> viewModel.moveCard(card, newSection) },
                            onDelete = { viewModel.deleteCard(card) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KanbanSectionSwitch(
    selectedSection: KanbanSection,
    onSectionSelected: (KanbanSection) -> Unit
) {
    val sections = KanbanSection.entries.toTypedArray()
    val selectedIndex = sections.indexOf(selectedSection)
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val sectionWidth = screenWidth / sections.size
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(24.dp)
            )
    ) {
        // Background segments
        Row(modifier = Modifier.fillMaxSize()) {
            sections.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }

        // Animated selector
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(sectionWidth.dp)
                .padding(4.dp)
                .offset(x = (sectionWidth * selectedIndex).dp)
                .background(
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(20.dp)
                )
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = 0.8f,
                        stiffness = Spring.StiffnessLow
                    )
                )
        )

        // Section labels and click targets
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            sections.forEachIndexed { index, section ->
                val isSelected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onSectionSelected(section) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = section.name,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun KanbanCard(
    card: KanbanCard,
    onMove: (KanbanSection) -> Unit,
    onDelete: () -> Unit = {}
) {
    var showDropdown by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this task?") },
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

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Task text
            Text(
                text = card.text,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Bottom row with Move To and Delete buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Move To button with dropdown
                Box {
                    TextButton(onClick = { showDropdown = true }) {
                        Text("Move to")
                    }
                    
                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false }
                    ) {
                        KanbanSection.entries.forEach { section ->
                            if (section != card.section) {
                                DropdownMenuItem(
                                    text = { Text("Move to ${section.name}") },
                                    onClick = {
                                        onMove(section)
                                        showDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                // Delete button
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
    }
} 