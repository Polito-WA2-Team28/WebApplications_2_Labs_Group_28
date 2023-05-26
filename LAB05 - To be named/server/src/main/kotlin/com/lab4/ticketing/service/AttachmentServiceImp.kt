package com.lab4.ticketing.service

import com.lab4.ticketing.dto.AttachmentDTO
import com.lab4.ticketing.dto.toDTO
import com.lab4.ticketing.model.Attachment
import com.lab4.ticketing.repository.AttachmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AttachmentServiceImp @Autowired constructor(private val attachmentRepository: AttachmentRepository) : AttachmentService {
    fun createAttachment(attachmentDTO: AttachmentDTO): Boolean{
        val attachment = Attachment(
            fileName = attachmentDTO.fileName,
            contentType = attachmentDTO.contentType,
            fileData = attachmentDTO.fileData
        )
        try {
            attachmentRepository.save(attachment)
            return true
        }catch(error: Error) {
            return false
        }
    }

    fun getAttachmentsById(id: Int): AttachmentDTO? {
        return attachmentRepository.findByIdOrNull(id)?.toDTO()
    }

    fun deleteAttachment(id: Int) {
        attachmentRepository.deleteById(id)
    }
}