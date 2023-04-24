package com.lab3.ticketing.model

import com.lab3.server.model.EntityBase
import com.lab3.server.model.Product
import com.lab3.server.model.Profile
import com.lab3.ticketing.util.TicketState
import jakarta.persistence.*


@Entity
@Table
class Ticket(
    var state: TicketState,
    @ManyToOne(fetch = FetchType.LAZY) var customer: Profile,
    @ManyToOne(fetch = FetchType.LAZY) var expert:Profile,
    var description:String,
    @ManyToOne(fetch = FetchType.LAZY) var product: Product,
    @OneToMany(fetch = FetchType.LAZY) var messages: MutableSet<Message>

): EntityBase<Long>() {

}