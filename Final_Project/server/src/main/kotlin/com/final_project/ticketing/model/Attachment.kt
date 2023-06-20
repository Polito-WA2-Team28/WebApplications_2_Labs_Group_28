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
    @Column(name = "fileUniqueName")
    var fileUniqueName: String

): EntityBase<Long>() {
}

fun MultipartFile.toModel(fileUniqueName: String): Attachment{
    return Attachment(originalFilename.orEmpty(), contentType.orEmpty(), fileUniqueName)
}