package com.final_project.ticketing.dto

import java.util.Date

class MessageDTO(
    val messageID: Long?, val messageText: String, val sender: String,
    val timestamp: Date, val attachmentsNames: List<String>
) {
}