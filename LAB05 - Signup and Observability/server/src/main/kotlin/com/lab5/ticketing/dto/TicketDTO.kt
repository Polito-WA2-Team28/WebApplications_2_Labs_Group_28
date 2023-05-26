package com.lab5.ticketing.dto

import com.lab5.ticketing.model.Ticket
import com.lab5.ticketing.util.TicketState
import java.util.Date
import java.util.UUID

data class TicketDTO(
    val ticketId:Long?, val ticketState:TicketState, val description:String, val serialNumber:Long,
    val customerId:UUID?, val expertId: UUID?, val creationDate:Date, val lastModified:Date) {
}


fun Ticket.toDTO() : TicketDTO {
    return TicketDTO(this.getId(), state, description, this.product.serialNumber,
                     this.customer.getId(), this.expert?.getId(), this.creationDate, this.lastModified)
}