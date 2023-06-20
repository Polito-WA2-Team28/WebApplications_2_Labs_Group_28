package com.final_project.ticketing.dto

import com.final_project.ticketing.model.Attachment

data class AttachmentDTO(val attachmentId:Long?, val fileName:String, val contentType:String, var fileUniqueName: String) {
}


fun Attachment.toDTO() : AttachmentDTO {
    return AttachmentDTO(this.getId(), this.fileName, this.contentType, this.fileUniqueName)
}