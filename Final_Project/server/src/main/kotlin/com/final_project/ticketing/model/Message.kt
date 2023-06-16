package com.final_project.ticketing.model

import com.final_project.server.model.EntityBase
import com.final_project.ticketing.dto.MessageObject
import jakarta.persistence.*
import org.springframework.web.multipart.MultipartFile
import java.util.Date

@Entity
@Table
class Message(
    var messageText:String,
    @Temporal(value=TemporalType.TIMESTAMP) var timestamp:Date,
    var sender:String,
    @OneToMany(cascade = [CascadeType.ALL])
    var attachmentSet:MutableSet<Attachment>,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    var ticket:Ticket

): EntityBase<Long>() {}


fun MessageObject.toModel(attachments:MutableSet<Attachment>, sender:String?, ticket:Ticket): Message{
    val date = Date()

    return Message(this.messageText, date, sender.orEmpty(), attachments, ticket)
}