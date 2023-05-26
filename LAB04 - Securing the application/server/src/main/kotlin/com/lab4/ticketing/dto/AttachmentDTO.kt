package com.lab4.ticketing.dto

import com.lab4.ticketing.model.Attachment

data class AttachmentDTO(val attachmentId:Long?, val fileName:String, val contentType:String, var fileData: ByteArray) {
}


fun Attachment.toDTO() : AttachmentDTO {
    return AttachmentDTO(this.getId(), this.fileName, this.contentType, this.fileData)
}