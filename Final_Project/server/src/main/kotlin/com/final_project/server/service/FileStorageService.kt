package com.final_project.server.service

import com.final_project.server.config.GlobalConfig
import com.final_project.ticketing.model.Attachment
import com.final_project.ticketing.model.toModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
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

    //Method to persist files
    fun persistAttachmentFile(attachment:MultipartFile): Attachment? {
        var uniqueFilename = UUID.randomUUID().toString() + "_" + attachment.originalFilename
        val filePath = File.separator + globalConfig.attachmentsDirectory + File.separator + uniqueFilename

        //is it right when it will be deployed? user.dir should use the workdir path
        attachment.transferTo(File(System.getProperty("user.dir") + filePath))

        //TO BE MODIFIED WITH THE URL THAT WILL RETRIEVE THE ATTACHMENT
        val attachmentUrl = File.separator + globalConfig.attachmentsDirectory + File.separator + "$uniqueFilename"

        return if (attachment.originalFilename != null && attachment.contentType != null) {
            attachment.toModel(attachmentUrl)
        } else{
            //What is better to return? throw exception because of empty attachment?
            null
        }
    }
}