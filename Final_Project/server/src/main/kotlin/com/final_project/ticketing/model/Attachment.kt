package com.final_project.ticketing.model

import com.final_project.server.model.EntityBase
import jakarta.persistence.*
import org.springframework.web.multipart.MultipartFile

@Entity
@Table
class Attachment(

    @Column(name = "fileName")
    var fileName: String,
    @Column(name = "contentType")
    var contentType: String,
    @Column(name = "fileURI")
    var fileURI: String

): EntityBase<Long>() {
}

fun MultipartFile.toModel(fileURI: String): Attachment{
    return Attachment(originalFilename.orEmpty(), contentType.orEmpty(), fileURI)
}