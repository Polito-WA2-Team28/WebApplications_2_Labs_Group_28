package com.lab5.ticketing.dto

import java.sql.Timestamp
import java.util.Date

class MessageDTO(
    val messageID: Long?, val messageText: String, val sender: String,
    val timestamp: Date, val attachmentsNames: List<String>
) {
}