package com.lab3.ticketing.model

import com.lab3.server.model.EntityBase
import jakarta.persistence.*
import java.util.Date

@Entity
@Table
class Message(
    var messageText:String,
    @Temporal(value=TemporalType.TIMESTAMP) var timestamp:Date,
    var sender:String,
    @OneToMany var attachmentSet:MutableSet<Attachment>,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    var ticket:Ticket

): EntityBase<Long>() {
}