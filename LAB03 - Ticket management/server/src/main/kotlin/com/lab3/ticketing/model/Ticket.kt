package com.lab3.ticketing.model

import com.lab3.server.dto.CustomerDTO
import com.lab3.server.dto.CustomerFormModification
import com.lab3.server.dto.ProductDTO
import com.lab3.server.model.*
import com.lab3.ticketing.dto.TicketCreationData
import com.lab3.ticketing.util.TicketState
import jakarta.persistence.*
import java.util.Date


@Entity
@Table
class Ticket(
    var state: TicketState,
    @ManyToOne(fetch = FetchType.LAZY) var customer: Customer,
    @ManyToOne(fetch = FetchType.LAZY) var expert:Expert?,
    var description:String,
    @ManyToOne(fetch = FetchType.LAZY) var product: Product,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticket", cascade = [CascadeType.MERGE])
    var messages: MutableSet<Message>,

    @Temporal(value = TemporalType.DATE)
    var creationDate:Date,

    @Temporal(value = TemporalType.DATE)
    var lastModified:Date

): EntityBase<Long>() {

}


fun TicketCreationData.toModel(customer: Customer, product: Product): Ticket {
    val date = Date()
    return Ticket(TicketState.OPEN, customer, null, description, product, mutableSetOf(), date, date)
}