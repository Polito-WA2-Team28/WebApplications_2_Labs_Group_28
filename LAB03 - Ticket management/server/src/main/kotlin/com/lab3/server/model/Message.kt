package com.lab3.server.model

import jakarta.persistence.*
import java.util.Date

@Entity
@Table
class Message(
    var messageText:String,
    @Temporal(value=TemporalType.TIMESTAMP) var timestamp:Date,
    @ManyToOne var sender:Profile,
    @OneToMany var attachmentSet:MutableSet<Attachment>

):EntityBase<Long>() {
}