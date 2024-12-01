package com.vortex.mail_man_1.viewmodel

import androidx.lifecycle.ViewModel
import com.vortex.mail_man_1.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel responsible for managing notes data and operations
 */
class NotesViewModel : ViewModel() {
    // StateFlow to handle the list of notes
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes = _notes.asStateFlow()

    /**
     * Adds a new text note to the list
     */
    fun addTextNote(title: String, content: String) {
        val note = TextNote(title = title, content = content)
        _notes.value += note
    }

    /**
     * Adds a new checklist note with multiple items
     */
    fun addChecklistNote(title: String, items: List<String>) {
        val checklistItems = items.filter { it.isNotBlank() }
            .map { ChecklistItem(text = it) }
        val note = ChecklistNote(title = title, items = checklistItems)
        _notes.value += note
    }

    /**
     * Adds a new canvas note with drawing
     */
    fun addCanvasNote(title: String, canvasUri: String) {
        val note = CanvasNote(title = title, canvasUri = canvasUri)
        _notes.value += note
    }

    /**
     * Deletes a note by its ID
     */
    fun deleteNote(noteId: String) {
        _notes.value = _notes.value.filter { it.id != noteId }
    }
} 