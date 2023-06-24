package com.final_project.ticketing.model

import com.final_project.server.dto.CustomerDTO
import com.final_project.server.dto.ProductDTO
import com.final_project.server.model.*
import com.final_project.ticketing.dto.TicketCreationData
import com.final_project.ticketing.dto.TicketDTO
import com.final_project.ticketing.util.TicketState
import jakarta.persistence.*
import java.util.*


@Entity
@Table
class Ticket(
    var state: TicketState,
    @ManyToOne(fetch = FetchType.LAZY) var customer: Customer,
    @ManyToOne(fetch = FetchType.LAZY) var expert:Expert?,
    var description:String,
    @ManyToOne(fetch = FetchType.LAZY) var product: Product,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticket", cascade = [CascadeType.MERGE], orphanRemoval = true)
    var messages: MutableSet<Message>,

    @Temporal(value = TemporalType.DATE)
    var creationDate:Date,

    @Temporal(value = TemporalType.DATE)
    var lastModified:Date

): EntityBase<Long>() {

    fun assignExpert(expert: Expert): Ticket {
        this.expert = expert
        return this
    }

    fun relieveExpert(): Ticket {
        this.expert = null
        return this
    }
}


fun TicketCreationData.toModel(customer: Customer, product: Product): Ticket {
    val date = Date()
    return Ticket(TicketState.OPEN, customer, null, description, product, mutableSetOf(), date, date)
}
