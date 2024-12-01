package com.vortex.mail_man_1.model

import java.util.UUID

sealed class Note {
    abstract val id: String
    abstract val title: String
    abstract val timestamp: Long
}

data class TextNote(
    override val id: String = UUID.randomUUID().toString(),
    override val title: String,
    override val timestamp: Long = System.currentTimeMillis(),
    val content: String
) : Note()

data class ChecklistNote(
    override val id: String = UUID.randomUUID().toString(),
    override val title: String,
    override val timestamp: Long = System.currentTimeMillis(),
    val items: List<ChecklistItem>
) : Note()

data class ChecklistItem(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    var isChecked: Boolean = false
)

data class ImageNote(
    override val id: String = UUID.randomUUID().toString(),
    override val title: String,
    override val timestamp: Long = System.currentTimeMillis(),
    val imageUri: String
) : Note()

data class CanvasNote(
    override val id: String = UUID.randomUUID().toString(),
    override val title: String,
    override val timestamp: Long = System.currentTimeMillis(),
    val canvasUri: String
) : Note() 