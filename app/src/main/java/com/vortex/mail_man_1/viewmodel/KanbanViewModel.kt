package com.vortex.mail_man_1.viewmodel

import androidx.lifecycle.ViewModel
import com.vortex.mail_man_1.model.KanbanCard
import com.vortex.mail_man_1.model.KanbanSection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class KanbanViewModel : ViewModel() {
    private val _cards = MutableStateFlow<List<KanbanCard>>(emptyList())
    val cards = _cards.asStateFlow()

    private val _selectedSection = MutableStateFlow(KanbanSection.TODO)
    val selectedSection = _selectedSection.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun addCard(text: String) {
        val newCard = KanbanCard(text = text, section = KanbanSection.TODO)
        _cards.value = _cards.value + newCard
    }

    fun moveCard(card: KanbanCard, newSection: KanbanSection) {
        if (newSection == KanbanSection.DOING && 
            _cards.value.count { it.section == KanbanSection.DOING } >= 2) {
            _errorMessage.value = "Do not start multiple things which you cannot finish"
            return
        }

        _cards.value = _cards.value.map { 
            if (it.id == card.id) it.copy(section = newSection) else it 
        }
        _errorMessage.value = null
    }

    fun selectSection(section: KanbanSection) {
        _selectedSection.value = section
    }

    fun clearError() {
        _errorMessage.value = null
    }
} 