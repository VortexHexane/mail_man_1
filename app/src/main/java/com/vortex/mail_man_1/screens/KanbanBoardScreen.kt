package com.vortex.mail_man_1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vortex.mail_man_1.model.KanbanCard
import com.vortex.mail_man_1.model.KanbanSection
import com.vortex.mail_man_1.viewmodel.KanbanViewModel

@Composable
fun KanbanBoardScreen(viewModel: KanbanViewModel = viewModel()) {
    val cards by viewModel.cards.collectAsState()
    val selectedSection by viewModel.selectedSection.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var newCardText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Kanban Board",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Section Toggle
        ScrollableTabRow(
            selectedTabIndex = KanbanSection.values().indexOf(selectedSection),
            modifier = Modifier.fillMaxWidth()
        ) {
            KanbanSection.values().forEach { section ->
                Tab(
                    selected = selectedSection == section,
                    onClick = { viewModel.selectSection(section) },
                    text = { Text(section.name) }
                )
            }
        }

        // Error Message
        errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // New Card Input (only in TODO section)
        if (selectedSection == KanbanSection.TODO) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newCardText,
                    onValueChange = { newCardText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("New task...") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (newCardText.isNotBlank()) {
                            viewModel.addCard(newCardText)
                            newCardText = ""
                        }
                    }
                ) {
                    Text("Add")
                }
            }
        }

        // Cards List
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cards.filter { it.section == selectedSection }) { card ->
                KanbanCard(
                    card = card,
                    onMove = { newSection -> viewModel.moveCard(card, newSection) }
                )
            }
        }
    }
}

@Composable
private fun KanbanCard(
    card: KanbanCard,
    onMove: (KanbanSection) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(card.text)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                KanbanSection.values().forEach { section ->
                    if (section != card.section && section != KanbanSection.TODO) {
                        TextButton(
                            onClick = { onMove(section) }
                        ) {
                            Text("Move to ${section.name}")
                        }
                    }
                }
            }
        }
    }
} 