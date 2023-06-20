package com.final_project.ticketing.dto

import com.final_project.ticketing.model.Message
import java.util.Date

class MessageDTO(
    val messageID: Long?, val messageText: String, val sender: String,
    val timestamp: Date, val attachmentsNames: List<String>
) {
}

fun Message.toDTO(): MessageDTO{
    return MessageDTO(
        this.getId(),
        this.messageText,
        this.sender,
        this.timestamp,
        this.attachmentSet
            .map {
                it -> it.fileUniqueName
            }
    )
}