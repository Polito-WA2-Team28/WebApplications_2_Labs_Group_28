package com.lab3.ticketing.model

import com.lab3.server.model.*
import com.lab3.ticketing.util.TicketState
import jakarta.persistence.*


@Entity
@Table
class Ticket(
    var state: TicketState,
    @ManyToOne(fetch = FetchType.LAZY) var customer: Customer,
    @ManyToOne(fetch = FetchType.LAZY) var expert:Expert,
    var description:String,
    @ManyToOne(fetch = FetchType.LAZY) var product: Product,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticket")
    var messages: MutableSet<Message>

): EntityBase<Long>() {

}