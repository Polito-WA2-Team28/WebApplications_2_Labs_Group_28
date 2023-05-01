package com.lab3.ticketing.dto

import com.lab3.ticketing.model.Attachment
import com.lab3.ticketing.model.Ticket
import com.lab3.ticketing.util.TicketState
import java.util.*

data class AttachmentDTO(val attachmentId:Long?, val fileName:String, val contentType:String, var fileData: ByteArray) {
}


fun Attachment.toDTO() : AttachmentDTO {
    return AttachmentDTO(this.getId(), this.fileName, this.contentType, this.fileData)
}