package com.lab5.ticketing.model

import com.lab5.server.model.EntityBase
import jakarta.persistence.*

@Entity
@Table
class Attachment(

    @Column(name = "fileName")
    var fileName: String,
    @Column(name = "contentType")
    var contentType: String,
    @Lob
    @Column(name = "fileData")
    var fileData: ByteArray

): EntityBase<Long>() {
}