package com.vortex.mail_man_1.model

import java.util.UUID

data class KanbanCard(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    var section: KanbanSection
)

enum class KanbanSection {
    TODO,
    DOING,
    CHECK,
    DONE
} 