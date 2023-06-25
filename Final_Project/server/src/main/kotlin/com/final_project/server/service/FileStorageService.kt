package com.final_project.server.service

import com.final_project.server.config.GlobalConfig
import com.final_project.ticketing.model.Attachment
import com.final_project.ticketing.model.toModel
import org.apache.commons.io.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.util.*

@Service
class FileStorageService @Autowired constructor(private val globalConfig: GlobalConfig) {

    init {
        createAttachmentsDirectoryIfNotExists()
    }

    private fun createAttachmentsDirectoryIfNotExists() {
        val directory = File(globalConfig.attachmentsDirectory)
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }


    fun persistAttachmentFile(attachment:MultipartFile): Attachment? {
        var uniqueFilename = UUID.randomUUID().toString() + "_" + attachment.originalFilename
        val filePath = File.separator + globalConfig.attachmentsDirectory + File.separator + uniqueFilename

        attachment.transferTo(File(System.getProperty("user.dir") + filePath))

        return if (attachment.originalFilename != null && attachment.contentType != null) {
            attachment.toModel(uniqueFilename)
        } else{
            //What is better to return? throw exception because of empty attachment?
            null
        }
    }

    fun getAttachmentFile(fileUniqueName: String): ResponseEntity<ByteArray> {
        try {
            val filePath = System.getProperty("user.dir") + File.separator + globalConfig.attachmentsDirectory + File.separator + fileUniqueName
            val file = File(filePath)
            val content:ByteArray = FileUtils.readFileToByteArray(file)

            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_OCTET_STREAM
            headers.contentDisposition = ContentDisposition.attachment().filename(file.name).build()

            return ResponseEntity(content, headers, HttpStatus.OK)

        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
    }
}