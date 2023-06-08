package com.lab5.ticketing.dto

import org.springframework.web.multipart.MultipartFile
import java.util.*

data class MessageObject(
    // sender and date are computed by the backend
    val messageText:String, val attachments:List<MultipartFile>
) {
}