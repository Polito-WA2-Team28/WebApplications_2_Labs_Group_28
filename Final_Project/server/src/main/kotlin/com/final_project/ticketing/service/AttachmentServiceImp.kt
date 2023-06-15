package com.final_project.ticketing.service

import com.final_project.ticketing.dto.AttachmentDTO
import com.final_project.ticketing.dto.toDTO
import com.final_project.ticketing.model.Attachment
import com.final_project.ticketing.repository.AttachmentRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
@Transactional
class AttachmentServiceImp @Autowired constructor(private val attachmentRepository: AttachmentRepository) : AttachmentService {
    fun createAttachment(attachmentDTO: AttachmentDTO): Boolean{
        val attachment = Attachment(attachmentDTO.fileName, attachmentDTO.contentType, attachmentDTO.fileURI)
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