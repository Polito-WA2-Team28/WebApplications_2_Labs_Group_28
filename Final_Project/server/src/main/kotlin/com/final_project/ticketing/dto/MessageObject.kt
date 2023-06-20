package com.final_project.ticketing.dto

import org.springframework.web.multipart.MultipartFile

data class MessageObject(
    // sender and date are computed by the backend
    val messageText:String,
    val attachments: List<MultipartFile>?
) {
}